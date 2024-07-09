package org.mediasoft.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.entity.enums.Category;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.mediasoft.warehouse.dto.RequestCreateProductDto;
import org.mediasoft.warehouse.dto.RequestUpdateProductDto;
import org.mediasoft.warehouse.dto.ResponseProductDto;
import org.mediasoft.warehouse.exceptions.SkuIsExistException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final ProductRepository productRepository;

    @SneakyThrows
    public ResponseProductDto createProduct(@Validated RequestCreateProductDto request) {

        if (productRepository.findByArticle(request.getArticle()).isPresent()) {
            throw new SkuIsExistException(String.valueOf(request.getArticle()));
        }

        var product = ProductEntity.builder()
                .price(request.getPrice())
                .article(request.getArticle())
                .name(request.getName())
                .category(Category.valueOf(request.getCategory()))
                .dictionary(request.getDictionary())
                .insertedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .qty(request.getQty())
                .build();

        product = productRepository.save(product);

        log.info("Product created successfully: {}", product);

        return convertToDto(product);
    }

    public ResponseProductDto getProductByArticle(UUID article) {
        var product = getProduct(article);

        log.info("Product getted successfully: {}", product.toString());

        return convertToDto(product);
    }

    public List<ResponseProductDto> getAllProducts(Pageable pageable) {
        var productsList = productRepository.findAll(pageable);

        log.info("Products getted successfully: {}", productsList.getSize());

        return productsList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @SneakyThrows
    public ResponseProductDto updateProduct(@Validated RequestUpdateProductDto request) {
        var product = getProduct(request.getOldArticle());

        if (request.getNewArticle() != null && !request.getNewArticle().equals(product.getArticle())) {
            if (productRepository.findByArticle(request.getNewArticle()).isPresent()) {
                throw new SkuIsExistException(String.valueOf(request.getNewArticle()));
            }
            product.setArticle(request.getNewArticle());
        }

        if (request.getName() != null && !request.getName().equals(product.getName())) {
            product.setName(request.getName());
        }

        if (request.getPrice() != null && !request.getPrice().equals(product.getPrice())) {
            product.setPrice(request.getPrice());
        }

        if (request.getCategory() != null && !request.getCategory().equals(product.getCategory().name())) {
            product.setCategory(Category.valueOf(request.getCategory()));
        }

        if (request.getDictionary() != null && !request.getDictionary().equals(product.getDictionary())) {
            product.setDictionary(request.getDictionary());
        }

        if (request.getQty() != null && !request.getQty().equals(product.getQty())) {
            product.setQty(request.getQty());
            product.setUpdatedAt(LocalDateTime.now());
        }

        product = productRepository.save(product);

        log.info("Product updated successfully: {}", product);

        return convertToDto(product);
    }

    @Transactional
    public void deleteByArticle(UUID article) {
        var product = getProduct(article);

        productRepository.delete(product);

        log.info("Product deleted successfully: {}", product);
    }

    private ProductEntity getProduct(UUID article) {
        return productRepository.findByArticle(article)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.valueOf(article)));
    }


    private ResponseProductDto convertToDto(ProductEntity product) {
        return ResponseProductDto.builder()
                .name(product.getName())
                .qty(product.getQty())
                .price(product.getPrice())
                .article(product.getArticle())
                .createdAt(product.getInsertedAt())
                .category(product.getCategory().name())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
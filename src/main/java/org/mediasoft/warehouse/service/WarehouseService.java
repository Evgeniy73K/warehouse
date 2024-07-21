package org.mediasoft.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.mappers.ProductMapper;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.entity.enums.Category;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.mediasoft.warehouse.controller.dto.RequestCreateProductDto;
import org.mediasoft.warehouse.controller.dto.RequestUpdateProductDto;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;
import org.mediasoft.warehouse.exceptions.SkuIsExistException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final ProductRepository productRepository;

    public ResponseProductDto createProduct(RequestCreateProductDto requestCreateProductDto) {

        var createProductDto = ProductMapper.INSTANCE.toCreateProductDto(requestCreateProductDto);


        if (productRepository.findByArticle(createProductDto.getArticle()).isPresent()) {
            throw new SkuIsExistException(String.valueOf(createProductDto.getArticle()));
        }

        var product = ProductMapper.INSTANCE.toProductEntity(createProductDto);

        product = productRepository.save(product);

        log.info("Product created successfully: {}", product);

        return ProductMapper.INSTANCE.toResponseProductDto(product);
    }

    public ResponseProductDto getProductById(UUID article) {
        var product = getProduct(article);

        log.info("Product getted successfully: {}", product.toString());

        return ProductMapper.INSTANCE.toResponseProductDto(product);
    }

    public List<ResponseProductDto> getAllProducts(Pageable pageable) {
        var productsList = productRepository.findAll(pageable);

        log.info("Products getted successfully: {}", productsList.getSize());

        return productsList.stream()
                .map(ProductMapper.INSTANCE::toResponseProductDto)
                .collect(Collectors.toList());
    }

    public ResponseProductDto updateProduct(RequestUpdateProductDto request) {
        var updateProductDto = ProductMapper.INSTANCE.toUpdateProductDto(request);

        final var product = getProduct(updateProductDto.getId());

        Optional.ofNullable(updateProductDto.getArticle())
                .filter(article -> !article.equals(product.getArticle()))
                .ifPresent(article -> {
                    if (productRepository.findByArticle(article).isPresent()) {
                        throw new SkuIsExistException(String.valueOf(article));
                    }
                    product.setArticle(article);
                });

        Optional.ofNullable(updateProductDto.getName())
                .filter(name -> !name.equals(product.getName()))
                .ifPresent(product::setName);

        Optional.ofNullable(updateProductDto.getPrice())
                .filter(price -> !price.equals(product.getPrice()))
                .ifPresent(product::setPrice);

        Optional.ofNullable(updateProductDto.getCategory())
                .filter(category -> !category.equals(product.getCategory().name()))
                .ifPresent(category -> product.setCategory(Category.valueOf(category)));

        Optional.ofNullable(updateProductDto.getDictionary())
                .filter(dictionary -> !dictionary.equals(product.getDictionary()))
                .ifPresent(product::setDictionary);

        Optional.ofNullable(updateProductDto.getQty())
                .filter(qty -> !qty.equals(product.getQty()))
                .ifPresent(qty -> {
                    product.setQty(qty);
                    product.setLastQtyChanged(LocalDateTime.now());
                });

        productRepository.save(product);

        log.info("Product updated successfully: {}", product);

        return ProductMapper.INSTANCE.toResponseProductDto(product);
    }


    @Transactional
    public void deleteById(UUID id) {
        var product = getProduct(id);

        productRepository.delete(product);

        log.info("Product deleted successfully: {}", product);
    }

    private ProductEntity getProduct(UUID id) {
        return productRepository.findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.valueOf(id)));
    }

}
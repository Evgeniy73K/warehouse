package org.mediasoft.warehouse.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.controller.dto.SearchDto;
import org.mediasoft.warehouse.mappers.ProductMapper;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.entity.enums.Category;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;
import org.mediasoft.warehouse.exceptions.SkuIsExistException;
import org.mediasoft.warehouse.service.dto.CreateProductDto;
import org.mediasoft.warehouse.service.dto.UpdateProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mediasoft.warehouse.controller.dto.enums.SearchEnum.EQUAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final EntityManager em;

    private final ProductRepository productRepository;

    public ResponseProductDto createProduct(CreateProductDto createProductDto) {
        var productList = productRepository.findByArticle(createProductDto.getArticle());

        if (productList.isPresent()) {
            throw new SkuIsExistException(String.valueOf(createProductDto.getArticle()), productList.get().getId());
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

    public ResponseProductDto updateProduct(UpdateProductDto updateProductDto) {

        final var product = getProduct(updateProductDto.getId());

        Optional.ofNullable(updateProductDto.getArticle())
                .filter(article -> !article.equals(product.getArticle()))
                .ifPresent(article -> {
                    var productList = productRepository.findByArticle(article);
                    if (productRepository.findByArticle(article).isPresent()) {
                        throw new SkuIsExistException(String.valueOf(article), productList.get().getId());
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

    public List<ResponseProductDto> findProductEntitysByCriterias(List<SearchDto> searchDto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);

        Root<ProductEntity> productEntity = cq.from(ProductEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        searchDto.forEach(
                s -> {
                    switch (s.getOperation()) {
                        case EQUAL -> predicates.add(cb.equal(productEntity.get(s.getField()), s.getValue()));
                        case GRATER_THAN_OR_EQ ->
                                predicates.add(cb.greaterThanOrEqualTo(productEntity.get(s.getField()), (Comparable) s.getValue()));
                        case LESS_THAN_OR_EQ ->
                                predicates.add(cb.lessThanOrEqualTo(productEntity.get(s.getField()), (Comparable) s.getValue()));
                        case LIKE -> predicates.add(cb.like(productEntity.get(s.getField()), "%" + s.getValue() + "%"));
                        default -> throw new IllegalStateException("Unexpected value: " + s.getOperation());
                    }
                }
        );


        cq.where(predicates.toArray(new Predicate[0]));

        var result = em.createQuery(cq).getResultList();
        return result.stream()
                .map(ProductMapper.INSTANCE::toResponseProductDto)
                .collect(Collectors.toList());
    }

}
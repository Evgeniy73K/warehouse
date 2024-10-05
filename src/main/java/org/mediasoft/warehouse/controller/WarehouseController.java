package org.mediasoft.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.controller.dto.RequestCreateProductDto;
import org.mediasoft.warehouse.controller.dto.RequestUpdateProductDto;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;
import org.mediasoft.warehouse.controller.dto.RequestCriteriaDto;
import org.mediasoft.warehouse.mappers.CriteriaMapper;
import org.mediasoft.warehouse.mappers.ProductMapper;
import org.mediasoft.warehouse.service.WarehouseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseProductDto createProduct(@RequestBody @Valid RequestCreateProductDto request) {
        var createProductDto = ProductMapper.INSTANCE.toCreateProductDto(request);
        return warehouseService.createProduct(createProductDto);
    }

    @GetMapping
    public List<ResponseProductDto > getProducts(Pageable pageable) {
        return warehouseService.getAllProducts(pageable);
    }

    @GetMapping("{id}")
    public ResponseProductDto getProduct(@PathVariable UUID id) {
        return warehouseService.getProductById(id);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable UUID id) {
        warehouseService.deleteById(id);
    }

    @PatchMapping
    public ResponseProductDto updateProduct(@RequestBody @Valid RequestUpdateProductDto request) {
        var updateProductDto = ProductMapper.INSTANCE.toUpdateProductDto(request);
        return warehouseService.updateProduct(updateProductDto);
    }

    @PostMapping(value = "/search")
    public List<ResponseProductDto > findProducts(@RequestBody @Valid List<RequestCriteriaDto> requestCriteriaDto,
                                                  Pageable pageable) {
        var searchDto = CriteriaMapper.INSTANCE.toCriteriaDto(requestCriteriaDto);
        return warehouseService.findProductEntitysByCriterias(searchDto, pageable);
    }
}
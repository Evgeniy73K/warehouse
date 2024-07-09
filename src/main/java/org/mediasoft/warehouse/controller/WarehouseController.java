package org.mediasoft.warehouse.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.dto.RequestCreateProductDto;
import org.mediasoft.warehouse.dto.RequestUpdateProductDto;
import org.mediasoft.warehouse.dto.ResponseProductDto;
import org.mediasoft.warehouse.service.WarehouseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseProductDto createProduct(@RequestBody @Validated RequestCreateProductDto request) {
        return warehouseService.createProduct(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseProductDto > getProducts(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        return warehouseService.getAllProducts(PageRequest.of(from / size, size));
    }

    @GetMapping("{article}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseProductDto getProduct(@PathVariable UUID article) {
        return warehouseService.getProductByArticle(article);
    }

    @DeleteMapping("{article}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable UUID article) {
        warehouseService.deleteByArticle(article);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ResponseProductDto updateProduct(@RequestBody @Validated RequestUpdateProductDto request) {
        return warehouseService.updateProduct(request);
    }
}
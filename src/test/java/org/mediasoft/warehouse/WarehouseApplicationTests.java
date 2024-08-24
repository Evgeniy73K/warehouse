package org.mediasoft.warehouse;

import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.repository.ProductRepository;

import java.util.stream.Stream;

@RequiredArgsConstructor
class WarehouseApplicationTests {
    private final EasyRandom generator = new EasyRandom();

    ProductRepository productRepository;


    @Test
    void createProductTest() {
        var products = Stream.generate(() -> generator.nextObject(ProductEntity.class))
                .limit(1000000)
                .toList();
        productRepository.saveAll(products);
    }

}

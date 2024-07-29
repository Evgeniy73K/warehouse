package org.mediasoft.warehouse.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.annotations.Stopwatch;
import org.mediasoft.warehouse.db.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Slf4j
@Component
@Profile("!local")
@ConditionalOnProperty(value = "scheduler.enabled", havingValue = "true")
@RequiredArgsConstructor
public class SimpleSchedulerService {
    private final ProductRepository productRepository;

    @Value(value = "${scheduler.pricePercentage}")
    private BigDecimal pricePercentage;


    @Scheduled(fixedRateString = "${scheduler.timing}")
    @Transactional
    @Stopwatch
    public void simpleUpdatePrice() {
        log.info("Start simple scheduler");
        var products = productRepository.findAll();
        products.forEach(product ->
                product.setPrice(product.getPrice()
                        .add(product.getPrice().divide(BigDecimal.valueOf(100)).multiply(pricePercentage))
                        .setScale(2, RoundingMode.HALF_UP))

        );
        productRepository.saveAll(products);
        log.info("End simple scheduler");
    }

}
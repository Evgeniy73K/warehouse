package org.mediasoft.warehouse;

import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WarehouseApplicationTests {
    private final EasyRandom generator = new EasyRandom();

    ProductRepository productRepository;
    public static final java.math.MathContext MATH_CONTEXT_18 = new java.math.MathContext(18, RoundingMode.HALF_UP);


    @Test
    void test() {
        BigDecimal a = BigDecimal.valueOf(-510);

        System.out.println(a.compareTo(BigDecimal.ZERO) > 0);
        System.out.println(a.compareTo(BigDecimal.ZERO) >= 0);




    }




    private boolean equalsByValue(BigDecimal a, BigDecimal b) {
        if(a == null && b == null) {
            return true;
        }
        if(a != null && b == null) {
            return false;
        }
        if(a == null) {
            return false;
        }
        return a.compareTo(b) == 0;
    }

}

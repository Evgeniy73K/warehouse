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

import static org.mediasoft.warehouse.Test.ENROLL;

@RequiredArgsConstructor
class WarehouseApplicationTests {
    String test = "ENROLL";
    private final EasyRandom generator = new EasyRandom();

    ProductRepository productRepository;
    public static final java.math.MathContext MATH_CONTEXT_18 = new java.math.MathContext(18, RoundingMode.HALF_UP);


    @Test
    void test() {
        System.out.println(test.contains(ENROLL.name()));
        System.out.println(test.contains(ENROLL.toString()));
        System.out.println();




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

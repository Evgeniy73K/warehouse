package org.mediasoft.warehouse.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.mediasoft.warehouse.db.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class CriteriaUtility {
    public void handleLikeOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        switch (field) {
            case "insertedAt" -> {
                LocalDateTime dateValue = (LocalDateTime) value;
                predicates.add(cb.between(productEntity.get(field), dateValue.minusDays(3), dateValue.plusDays(3)));
            }
            case "price" -> {
                BigDecimal bigDecimalValue = BigDecimal.valueOf((Integer) value);
                BigDecimal range = bigDecimalValue.multiply(BigDecimal.valueOf(0.1));
                BigDecimal startPrice = bigDecimalValue.subtract(range);
                BigDecimal endPrice = bigDecimalValue.add(range);
                predicates.add(cb.between(productEntity.get(field), startPrice, endPrice));
            }
            default -> predicates.add(cb.like(productEntity.get(field), "%" + value + "%"));
        }
    }

    public void handleGraterThanOrEqOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        if (field.equals("name")) {
            predicates.add(cb.like(productEntity.get(field), value + "%"));
        } else {
            predicates.add(cb.greaterThanOrEqualTo(productEntity.get(field), (Comparable) value));
        }
    }

    public void handleLessThanOrEqOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        if (field.equals("name")) {
            predicates.add(cb.like(productEntity.get(field), "%" + value));
        } else {
            predicates.add(cb.lessThanOrEqualTo(productEntity.get(field), (Comparable) value));
        }
    }
}

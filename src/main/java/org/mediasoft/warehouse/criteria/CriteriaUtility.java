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
    private static final int DEFAULT_DATE_RANGE_DAYS = 3;
    private static final BigDecimal MIN_PRICE_DIFF = BigDecimal.valueOf(0.9);
    private static final BigDecimal MAX_PRICE_DIFF = BigDecimal.valueOf(1.1);
    private static final String FIELD_INSERTED_AT = "insertedAt";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_NAME = "name";

    public void handleLikeOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        switch (field) {
            case FIELD_INSERTED_AT -> {
                LocalDateTime dateValue = (LocalDateTime) value;
                predicates.add(cb.between(productEntity.get(field), dateValue.minusDays(DEFAULT_DATE_RANGE_DAYS), dateValue.plusDays(DEFAULT_DATE_RANGE_DAYS)));
            }
            case FIELD_PRICE -> {
                BigDecimal bigDecimalValue = BigDecimal.valueOf((Integer) value);
                predicates.add(cb.between(productEntity.get(field), bigDecimalValue.multiply(MIN_PRICE_DIFF),
                        bigDecimalValue.multiply(MAX_PRICE_DIFF)));
            }
            default -> predicates.add(cb.like(productEntity.get(field), "%" + value + "%"));
        }
    }

    public void handleGreaterThanOrEqOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        if (FIELD_NAME.equals(field)) {
            predicates.add(cb.like(productEntity.get(field), value + "%"));
        } else {
            predicates.add(cb.greaterThanOrEqualTo(productEntity.get(field), (Comparable) value));
        }
    }

    public void handleLessThanOrEqOperation(List<Predicate> predicates, CriteriaBuilder cb, Root<ProductEntity> productEntity, String field, Object value) {
        if (FIELD_NAME.equals(field)) {
            predicates.add(cb.like(productEntity.get(field), "%" + value));
        } else {
            predicates.add(cb.lessThanOrEqualTo(productEntity.get(field), (Comparable) value));
        }
    }
}

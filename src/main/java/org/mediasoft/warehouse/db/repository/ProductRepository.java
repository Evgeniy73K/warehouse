package org.mediasoft.warehouse.db.repository;

import org.mediasoft.warehouse.db.entity.ProductEntity;
import org.mediasoft.warehouse.db.projection.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByArticle(UUID article);

    @Query(nativeQuery = true, value = "SELECT p.id AS id, " +
            "p.name AS name, " +
            "op.qty AS qty, " +
            "op.price AS price " +
            "FROM product p " +
            "JOIN ordered_product op " +
            "ON p.id = op.product_id where order_id =:id")
    List<OrderSummary> getOrderDetailByOrderId(@Param("id")UUID id);
}

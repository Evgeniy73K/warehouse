package org.mediasoft.warehouse.db.repository;

import org.mediasoft.warehouse.db.entity.OrderedProductEntity;
import org.mediasoft.warehouse.db.entity.keys.OrderedProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductId> {

    @Query(nativeQuery = true, value = "SELECT * FROM ordered_product WHERE order_id = :id")
    Iterable<OrderedProductEntity> findAllByOrderId(@Param("id") UUID id);
}

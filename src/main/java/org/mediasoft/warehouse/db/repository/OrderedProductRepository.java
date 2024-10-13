package org.mediasoft.warehouse.db.repository;

import org.mediasoft.warehouse.db.entity.OrderedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, UUID> {
}

package org.mediasoft.warehouse.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mediasoft.warehouse.db.entity.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private UUID article;
    private String dictionary;
    private Category category;
    private BigDecimal price;
    private BigDecimal qty;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;
}



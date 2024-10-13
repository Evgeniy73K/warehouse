package org.mediasoft.warehouse.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mediasoft.warehouse.db.entity.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "article", nullable = false, unique = true)
    private UUID article;

    @Column(name = "dictionary")
    private String dictionary;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "qty")
    private BigDecimal qty;

    @CreationTimestamp
    @Column(name = "inserted_at", nullable = false)
    private LocalDateTime insertedAt;

    @UpdateTimestamp
    @Column(name = "last_qty_changed")
    private LocalDateTime lastQtyChanged;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
}



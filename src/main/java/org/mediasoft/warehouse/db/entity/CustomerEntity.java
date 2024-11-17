package org.mediasoft.warehouse.db.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @SequenceGenerator(name = "customerid", sequenceName = "customerid", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerid")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}

package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Describes ingredient that links to a source product
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ingredient")
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id"
    )
    private Product sourceProduct;

    @Column(
            name = "amount"
    )
    private Double amount;

    @Enumerated(EnumType.STRING)
    private Unit unit;

}

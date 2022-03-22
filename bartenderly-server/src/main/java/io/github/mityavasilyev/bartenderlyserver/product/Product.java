package io.github.mityavasilyev.bartenderlyserver.product;

import io.github.mityavasilyev.bartenderlyserver.extra.Unit;
import lombok.*;

import javax.persistence.*;

/**
 * Describes consumable that will be used for ingredient
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "product")
@Table(name = "products")
public class Product {

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

    @Column(
            name = "amount_left"
    )
    private Double amountLeft;

    @Enumerated(EnumType.STRING)
    private Unit unit;

}

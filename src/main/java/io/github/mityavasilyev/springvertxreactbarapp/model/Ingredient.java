package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Describes ingredient that links to a source product
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    private Long id;
    private String name;
    private String description;
    private Product sourceProduct;
    private Double amount;
    private Unit unit;

}

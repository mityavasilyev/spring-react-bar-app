package io.github.mityavasilyev.springvertxreactbarapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Describes consumable that will be used for ingredient
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double amountLeft;
    private Unit unit;

}

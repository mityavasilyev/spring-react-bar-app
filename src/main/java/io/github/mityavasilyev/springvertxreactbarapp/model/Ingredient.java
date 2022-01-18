package io.github.mityavasilyev.springvertxreactbarapp.model;

import io.github.mityavasilyev.springvertxreactbarapp.model.BaseEntity;
import io.github.mityavasilyev.springvertxreactbarapp.model.Product;
import io.github.mityavasilyev.springvertxreactbarapp.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

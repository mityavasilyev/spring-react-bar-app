package io.github.mityavasilyev.springvertxreactbarapp.model;

import io.github.mityavasilyev.springvertxreactbarapp.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

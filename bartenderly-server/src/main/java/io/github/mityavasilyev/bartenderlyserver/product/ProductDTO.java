package io.github.mityavasilyev.bartenderlyserver.product;

import io.github.mityavasilyev.bartenderlyserver.extra.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    String name;
    String description;
    Double amountLeft;
    Unit unit;

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @return parsed entity
     */
    public Product parseProduct() {
        return Product.builder()
                .name(this.name)
                .description(this.description)
                .amountLeft(this.amountLeft)
                .unit(this.unit)
                .build();
    }
}

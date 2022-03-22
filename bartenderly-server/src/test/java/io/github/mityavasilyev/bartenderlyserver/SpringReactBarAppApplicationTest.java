package io.github.mityavasilyev.bartenderlyserver;

import io.github.mityavasilyev.bartenderlyserver.cocktail.CocktailController;
import io.github.mityavasilyev.bartenderlyserver.product.ProductController;
import io.github.mityavasilyev.bartenderlyserver.tag.TagController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringReactBarAppApplicationTest {

    @Autowired
    ProductController productController;

    @Autowired
    CocktailController cocktailController;

    @Autowired
    TagController tagController;

    @Test
    void contextLoads() {
        assertNotNull(productController);
        assertNotNull(cocktailController);
        assertNotNull(tagController);
    }

}

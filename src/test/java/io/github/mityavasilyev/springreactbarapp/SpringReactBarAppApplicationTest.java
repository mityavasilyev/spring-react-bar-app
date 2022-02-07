package io.github.mityavasilyev.springreactbarapp;

import io.github.mityavasilyev.springreactbarapp.cocktail.CocktailController;
import io.github.mityavasilyev.springreactbarapp.product.ProductController;
import io.github.mityavasilyev.springreactbarapp.tag.TagController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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

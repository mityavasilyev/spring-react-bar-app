package io.github.mityavasilyev.springreactbarapp.bootstrap;

import io.github.mityavasilyev.springreactbarapp.cocktail.Cocktail;
import io.github.mityavasilyev.springreactbarapp.extra.*;
import io.github.mityavasilyev.springreactbarapp.cocktail.CocktailRepository;
import io.github.mityavasilyev.springreactbarapp.product.Product;
import io.github.mityavasilyev.springreactbarapp.product.ProductService;
import io.github.mityavasilyev.springreactbarapp.tag.TagRepository;
import io.github.mityavasilyev.springreactbarapp.tag.Tag;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Bootstrap class. Sorta playground
 */
@Profile("debug")
@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final TagRepository tagRepository;
    private final CocktailRepository cocktailRepository;
    private final ProductService productService;

    public DevBootstrap(TagRepository tagRepository,
                        CocktailRepository cocktailRepository,
                        ProductService productService) {
        this.tagRepository = tagRepository;
        this.cocktailRepository = cocktailRepository;
        this.productService = productService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        List<Tag> tags = new LinkedList<>();
        Tag rumTag = new Tag("Rum");
        tags.add(rumTag);
        Tag coldTag = new Tag("Cold");
        tags.add(coldTag);
        Tag sweetTag = new Tag("Sweet");
        tags.add(sweetTag);
        Tag bitterTag = new Tag("Bitter");
        tags.add(bitterTag);
        tagRepository.saveAll(Arrays.asList(rumTag, coldTag, sweetTag, bitterTag));

        List<Product> products = new LinkedList<>();
        Product cola = Product.builder()
                .name("Coca Cola")
                .amountLeft(2d)
                .unit(Unit.LITER)
                .build();
        products.add(cola);
        Product bacardi = Product.builder()
                .name("Bacardi Carta Blanca")
                .amountLeft(0.7d)
                .unit(Unit.LITER)
                .build();
        products.add(bacardi);
        Product lemon = Product.builder()
                .name("Lemon")
                .amountLeft(7d)
                .unit(Unit.PIECE)
                .description("Supa fresh")
                .build();
        products.add(lemon);
        products.stream().forEach(productService::addNew);

        Cocktail cocktail1 = Cocktail.builder()
                .name("Cuba Libre")
                .description("Taste some of that rum")
                .note("Empty note")
                .ingredients(new HashSet<>(Arrays.asList(
                        Ingredient.builder()
                                .name("Coca-Cola")
                                .amount(1d)
                                .unit(Unit.OUNCE)
                                .sourceProduct(cola)
                                .build(),
                        Ingredient.builder()
                                .name("Rum")
                                .amount(1d)
                                .unit(Unit.OUNCE)
                                .sourceProduct(bacardi)
                                .build()
                        )))
                .tags(new HashSet<>(Arrays.asList(
                        rumTag, sweetTag, coldTag
                )))
                .recipe(Recipe.builder().steps("Mix em together").build())
                .build();
        cocktailRepository.save(cocktail1);

        Cocktail cocktail2 = Cocktail.builder()
                .name("Mohito")
                .description("Summertime")
                .note("with alco")
                .ingredients(new HashSet<>(Arrays.asList(
                        Ingredient.builder()
                                .name("White Rum")
                                .amount(1d)
                                .unit(Unit.OUNCE)
                                .sourceProduct(bacardi)
                                .build(),
                        Ingredient.builder()
                                .name("Lemon")
                                .amount(2d)
                                .unit(Unit.PIECE)
                                .sourceProduct(lemon)
                                .build(),
                        Ingredient.builder()
                                .name("Soda")
                                .amount(4d)
                                .unit(Unit.OUNCE)
                                .build()
                )))
                .tags(new HashSet<>(Arrays.asList(
                        rumTag, coldTag
                )))
                .recipe(Recipe.builder().steps("Ya know the drill. Mix this stuff").build())
                .build();
        cocktailRepository.save(cocktail2);

        Cocktail cocktail3 = Cocktail.builder()
                .name("Aviator")
                .description("Bitter guy")
                .ingredients(new HashSet<>(Arrays.asList(
                        Ingredient.builder()
                                .name("Gin")
                                .amount(1d)
                                .unit(Unit.OUNCE)
                                .build(),
                        Ingredient.builder()
                                .name("Lemon")
                                .amount(2d)
                                .unit(Unit.PIECE)
                                .sourceProduct(lemon)
                                .build(),
                        Ingredient.builder()
                                .name("Soda")
                                .amount(4d)
                                .unit(Unit.OUNCE)
                                .build()
                )))
                .tags(new HashSet<>(Arrays.asList(
                        coldTag, bitterTag
                )))
                .recipe(Recipe.builder().steps("Pour gin first, then the rest").build())
                .build();
        cocktailRepository.save(cocktail3);
    }
}

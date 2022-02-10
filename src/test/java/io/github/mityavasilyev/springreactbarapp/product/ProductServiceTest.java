package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Map<Long, Product> dummyRepository;

    @BeforeEach
    void setUp() {
        dummyRepository = new HashMap<>();
        dummyRepository.put(1l,
                Product.builder()
                        .id(1l)
                        .name("Cola")
                        .amountLeft(2d)
                        .unit(Unit.LITER)
                        .description("Cold Fuzz")
                        .build());
        dummyRepository.put(2l,
                Product.builder()
                        .id(2l)
                        .name("Sugar")
                        .amountLeft(2d)
                        .unit(Unit.GRAM)
                        .description("Good for decoration")
                        .build());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
        List<Product> mockProducts = new ArrayList<>(dummyRepository.values());
        Mockito.when(productRepository.findAll())
                .thenReturn(mockProducts);
        List<Product> products = productService.getAll();
        assertEquals(mockProducts.size(), products.size());
    }

    @Test
    void getById() {
        Mockito.when(productRepository.findById(1l))
                .thenReturn(Optional.ofNullable(dummyRepository.get(1l)));
        Product product = productService.getById(1l);
        assertSame(dummyRepository.get(1l), product);
        assertEquals(dummyRepository.get(1l).getName(), product.getName());
    }

    @Test
    void getAllByName() {
        Mockito.when(productRepository.findByNameContainingIgnoreCase("a"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(product -> product.getName().contains("a"))
                        .toList());
        List<Product> products = productService.getAllByName("a");
        assertEquals(dummyRepository.size(), products.size());

        Mockito.when(productRepository.findByNameContainingIgnoreCase("b"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(product -> product.getName().contains("b"))
                        .toList());
        products = productService.getAllByName("b");
        assertEquals(0, products.size());
    }

    @Test
    void addNew() {
        Product product = Product.builder()
                .id(3l)
                .name("Test")
                .unit(Unit.OUNCE)
                .amountLeft(20d)
                .build();
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        Product returnedProduct = productService.addNew(product);
        assertEquals(product, returnedProduct);

        // conversion check
        Mockito.verify(productRepository)
                .save(Mockito.argThat(
                        (Product argTestProduct) -> argTestProduct.getAmountLeft().equals((double) 20 * 30)));

        Mockito.verify(productRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void deleteById() {
        productService.deleteById(1l);
        Mockito.verify(productRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());
    }

    @Test
    void updateById() {
        Mockito.when(productRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(Product.builder().build()));
        productService.updateById(1l, Product.builder().id(1l).build());
        Mockito.verify(productRepository, Mockito.times(1))
                .save(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1))
                .findById(1l);
    }

    @Test
    void consumeIngredients() throws UnitMismatchException, NotEnoughProductException {
        Product dummyProduct = dummyRepository.get(1l);
        Double originalAmountLeft = (dummyProduct.getAmountLeft() * 1000);  // mocking automatic conversion on save
        Ingredient ingredient = Ingredient.builder()
                .id(1l)
                .amount(1D)
                .unit(Unit.OUNCE)
                .sourceProduct(dummyProduct)
                .build();

        Mockito.when(productRepository.findById(1l))
                .thenReturn(Optional.ofNullable(dummyProduct));

        List<Product> returnedProducts = productService.consumeIngredients(Arrays.asList(ingredient));

        assertEquals(originalAmountLeft - 30, returnedProducts.get(0).getAmountLeft());

        Mockito.verify(productRepository, Mockito.times(1))
                .findById(Mockito.any());
    }

    @Test
    void consumeIngredients_unitMismatch() {
        Product dummyProduct = dummyRepository.get(2l);
        Ingredient ingredient = Ingredient.builder()
                .id(1l)
                .amount(1D)
                .unit(Unit.OUNCE)
                .sourceProduct(dummyProduct)
                .build();

        Mockito.when(productRepository.findById(2l))
                .thenReturn(Optional.ofNullable(dummyProduct));

        assertThrows(UnitMismatchException.class,
                () -> productService.consumeIngredients(Arrays.asList(ingredient)),
                "Expected to throw UnitMismatchException due to different units");
    }

    @Test
    void consumeIngredients_notEnoughProduct() {
        Product dummyProduct = dummyRepository.get(2l);
        Ingredient ingredient = Ingredient.builder()
                .id(1l)
                .amount(40D)
                .unit(Unit.GRAM)
                .sourceProduct(dummyProduct)
                .build();

        Mockito.when(productRepository.findById(2l))
                .thenReturn(Optional.ofNullable(dummyProduct));

        assertThrows(NotEnoughProductException.class,
                () -> productService.consumeIngredients(Arrays.asList(ingredient)),
                "Expected to throw NotEnoughProductException due to lack of product to consume");
    }
}
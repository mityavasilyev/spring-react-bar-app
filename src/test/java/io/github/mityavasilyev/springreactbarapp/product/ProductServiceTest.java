package io.github.mityavasilyev.springreactbarapp.product;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Map<Long, Product> productRepositoryMock;

    @BeforeEach
    void setUp() {
        productRepositoryMock = new HashMap<>();
        productRepositoryMock.put(1l,
                Product.builder()
                        .id(1l)
                        .name("Cola")
                        .amountLeft(2d)
                        .unit(Unit.LITER)
                        .description("Cold Fuzz")
                        .build());
        productRepositoryMock.put(2l,
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
        List<Product> mockProducts = new ArrayList<>(productRepositoryMock.values());
        Mockito.when(productRepository.findAll())
                .thenReturn(mockProducts);
        List<Product> products = productService.getAll();
        assertEquals(mockProducts.size(), products.size());
    }

    @Test
    void getById() {
        Mockito.when(productRepository.findById(1l))
                .thenReturn(Optional.ofNullable(productRepositoryMock.get(1l)));
        Product product = productService.getById(1l);
        assertSame(productRepositoryMock.get(1l), product);
        assertEquals(productRepositoryMock.get(1l).getName(), product.getName());
    }

    @Test
    void getAllByName() {
        Mockito.when(productRepository.findByNameContainingIgnoreCase("a"))
                .thenReturn(productRepositoryMock.values()
                        .stream()
                        .filter(product -> product.getName().contains("a"))
                        .toList());
        List<Product> products = productService.getAllByName("a");
        assertEquals(productRepositoryMock.size(), products.size());

        Mockito.when(productRepository.findByNameContainingIgnoreCase("b"))
                .thenReturn(productRepositoryMock.values()
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
        Mockito.verify(productRepository).save(Mockito.any());
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
    }

    @Test
    void consumeIngredients() {

    }
}
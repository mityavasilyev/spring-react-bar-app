package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.TestUtils;
import io.github.mityavasilyev.springreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    MockMvc mockMvc;

    private Product mockProduct;

    private final String BASE_URL = "/api/products";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockProduct = Product.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .amountLeft(50D)
                .unit(Unit.LITER)
                .build();
    }

    @Test
    void getAllProducts() throws Exception {
        Mockito.when(productService.getAll())
                .thenReturn(Arrays.asList(mockProduct));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists());
        Mockito.verify(productService, Mockito.times(1))
                .getAll();
    }

    @Test
    void getAllProductsByName() throws Exception {
        Mockito.when(productService.getAllByName(Mockito.any()))
                .thenReturn(Arrays.asList(mockProduct));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/name/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                        .value(mockProduct.getName()));
        Mockito.verify(productService, Mockito.times(1))
                .getAllByName(Mockito.any());
    }

    @Test
    void getProductById() throws Exception {
        Mockito.when(productService.getById(1L))
                .thenReturn(mockProduct);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
        Mockito.verify(productService, Mockito.times(1))
                .getById(Mockito.any());
    }

    @Test
    void getProductById_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/-20"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/0"))
                .andExpect(status().isBadRequest());
        Mockito.verify(productService, Mockito.times(0))
                .getById(Mockito.any());
    }

    @Test
    void addNewProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(TestUtils.toJson(mockProduct))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.verify(productService, Mockito.times(1))
                .addNew(Mockito.any());
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    void deleteCocktail_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/-20"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/0"))
                .andExpect(status().isBadRequest());
        Mockito.verify(productService, Mockito.times(0))
                .deleteById(Mockito.any());
    }

    @Test
    void updateProduct() throws Exception {
        Mockito.when(productService.getById(Mockito.any()))
                .thenReturn(mockProduct);
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .content(TestUtils.toJson(mockProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1))
                .updateById(Mockito.any(), Mockito.any());
    }

    @Test
    void updateProduct_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/-20")
                        .content(TestUtils.toJson(mockProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/0")
                        .content(TestUtils.toJson(mockProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(productService, Mockito.times(0))
                .updateById(Mockito.any(), Mockito.any());
    }

    @Test
    void consumeProducts() throws Exception {
        Mockito.when(productService.consumeIngredients(Mockito.any()))
                .thenReturn(Arrays.asList(mockProduct));
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/consume")
                .content(TestUtils.toJson(Arrays.asList(Ingredient.builder().id(1L).build())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
        Mockito.verify(productService, Mockito.times(1))
                .consumeIngredients(Mockito.any());
    }
}
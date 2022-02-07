package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Unit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/products")
public class ProductController extends ExceptionController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping(path = "/name/{name}")
    public List<Product> getAllProductsByName(@PathVariable("name") String name) {
        return productService.getAllByName(name);
    }

    @GetMapping(path = "{productId}")
    public Product getProductById(@PathVariable("productId") Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public void addNewProduct(@RequestBody ProductModel productModel) {
        productService.addNew(parseProduct(productModel));
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long id,
                                                 @RequestBody ProductModel productModel) {
        Product productPatch = parseProduct(productModel);
        Product product = productService.getById(id);
        if (product == null) return ResponseEntity.notFound().build();

        if (productPatch.getName() != null) product.setName(productPatch.getName());
        if (productPatch.getDescription() != null) product.setDescription(productPatch.getDescription());
        product = productService.updateById(id, product);

        return ResponseEntity.ok(product);
    }

    /**
     * Consumes provided list of ingredients if possible
     * If not, returns an error message with issue cause
     *
     * @param ingredients list of ingredients to consume
     * @return List of new products
     * @throws NotEnoughProductException handled by ExceptionController. Contains an error message
     */
    @PostMapping(path = "/consume")
    public ResponseEntity<Object> consumeProducts(@RequestBody List<Ingredient> ingredients)
            throws NotEnoughProductException, UnitMismatchException {

        List<Product> products = productService.consumeIngredients(ingredients);
        return ResponseEntity.ok(products);

    }

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @param productModel model that needs to parsed
     * @return parsed entity
     */
    private Product parseProduct(ProductModel productModel) {
        return Product.builder()
                .id(productModel.id)
                .name(productModel.name)
                .description(productModel.description)
                .amountLeft(productModel.amountLeft)
                .unit(productModel.unit)
                .build();
    }

    class ProductModel {
        Long id;
        String name;
        String description;
        Double amountLeft;
        Unit unit;
    }
}

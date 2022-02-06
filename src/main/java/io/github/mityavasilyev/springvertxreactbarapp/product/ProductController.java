package io.github.mityavasilyev.springvertxreactbarapp.product;

import io.github.mityavasilyev.springvertxreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springvertxreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springvertxreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springvertxreactbarapp.extra.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void addNewProduct(@RequestBody Product product) {
        productService.addNew(product);
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long id,
                                                 @RequestBody Product productPatch) {
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
}

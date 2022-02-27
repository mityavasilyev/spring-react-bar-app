package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.exceptions.InvalidIdException;
import io.github.mityavasilyev.springreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ROLE_BARTENDER"})
@RequestMapping(path = "api/products")
public class ProductController extends ExceptionController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<List<Product>> getAllProductsByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getAllByName(name));
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long id) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Product> addNewProduct(@RequestBody ProductDTO productDTO) {
        Product product = productDTO.parseProduct();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addNew(product));
    }

    @DeleteMapping(path = "{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long id,
                                                 @RequestBody ProductDTO productDTO) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        Product productPatch = productDTO.parseProduct();
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
    public ResponseEntity<List<Product>> consumeProducts(@RequestBody List<Ingredient> ingredients)
            throws NotEnoughProductException, UnitMismatchException {

        List<Product> products = productService.consumeIngredients(ingredients);
        return ResponseEntity.ok(products);

    }
}

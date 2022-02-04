package io.github.mityavasilyev.springvertxreactbarapp.product;

import io.github.mityavasilyev.springvertxreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springvertxreactbarapp.extra.Ingredient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @return all products
     */
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * @param id product id
     * @return product with matching id
     */
    public Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No product with such id");
        }
    }

    /**
     * Retrieves all products with name containing provided string
     *
     * @param name string to search for
     * @return matching products
     */
    public List<Product> getAllByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Saves provided product to the repository
     *
     * @param product to save
     * @return saved product
     */
    public Product addNew(Product product) {
        return productRepository.save(product);
    }

    /**
     * Deletes product with matching id
     *
     * @param id of product to delete
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Updates product with provided entity
     *
     * @param id         of product to update
     * @param newProduct new entity
     * @return updated entity
     */
    public Product updateById(Long id, Product newProduct) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.save(newProduct);
            return newProduct;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No product with such id");
        }
    }

    @Transactional(rollbackFor = {NotEnoughProductException.class})
    public List<Product> consume(Map<Product, Double> consumables) throws NotEnoughProductException {
        List<Product> affectedProducts = new ArrayList<>();
        for (Map.Entry<Product, Double> entry : consumables.entrySet()) {
            Product product = entry.getKey();
            Double amount = entry.getValue();

            affectedProducts.add(consumeOne(product.getId(), amount));
        }
        return affectedProducts;
    }

    private Product consumeOne(Long productId, Double amount) throws NotEnoughProductException {
        Product consumable = getById(productId);
        if (consumable.getAmountLeft() > 0 && !((consumable.getAmountLeft() - amount) < 0)) {
            consumable.setAmountLeft(
                    consumable.getAmountLeft() - amount
            );
            productRepository.save(consumable);
            return consumable;
        } else {
            throw new NotEnoughProductException(
                    String.format("Can't consume %s %s of %s (available: %s)",
                            amount.toString(),
                            consumable.getUnit().toString(),
                            consumable.getName(),
                            consumable.getAmountLeft().toString()
                    ));
        }
    }
}

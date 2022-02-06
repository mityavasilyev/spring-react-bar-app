package io.github.mityavasilyev.springvertxreactbarapp.product;

import io.github.mityavasilyev.springvertxreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springvertxreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springvertxreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springvertxreactbarapp.extra.Unit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        AmountUnit amountUnit = standardizeUnit(product.getAmountLeft(), product.getUnit());
        product.setAmountLeft(amountUnit.getAmount());
        product.setUnit(amountUnit.getUnit());
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
    public List<Product> consume(Map<Product, Ingredient> consumables)
            throws NotEnoughProductException, UnitMismatchException {
        List<Product> affectedProducts = new ArrayList<>();
        for (Map.Entry<Product, Ingredient> entry : consumables.entrySet()) {
            Product product = entry.getKey();
            Ingredient ingredient = entry.getValue();

            affectedProducts.add(consumeOne(product.getId(), ingredient));
        }
        return affectedProducts;
    }

    private Product consumeOne(Long productId, Ingredient ingredient)
            throws NotEnoughProductException, UnitMismatchException {
        Product consumable = getById(productId);
        AmountUnit productAmountUnit = standardizeUnit(consumable.getAmountLeft(), consumable.getUnit());
        AmountUnit ingredientAmountUnit = standardizeUnit(ingredient.getAmount(), ingredient.getUnit());
        if (!productAmountUnit.getUnit().equals(ingredientAmountUnit.getUnit()))
            throw new UnitMismatchException(
                    String.format(
                            "Can't consume %s because source product is in %s",
                            ingredientAmountUnit.getUnit(),
                            productAmountUnit.getUnit()));
        if (productAmountUnit.getAmount() > 0
                && !((productAmountUnit.getAmount() - ingredientAmountUnit.getAmount()) < 0)) {
            consumable.setAmountLeft(
                    productAmountUnit.getAmount() - ingredientAmountUnit.getAmount()
            );
            consumable.setUnit(productAmountUnit.getUnit());
            productRepository.save(consumable);
            return consumable;
        } else {
            throw new NotEnoughProductException(
                    String.format("Can't consume %s %s of %s (available: %s)",
                            ingredient.getUnit().toString(),
                            consumable.getUnit().toString(),
                            consumable.getName(),
                            consumable.getAmountLeft().toString()
                    ));
        }
    }

    private AmountUnit standardizeUnit(Double amount, Unit unit) {
        return switch (unit) {
            case MILLILITER, PIECE, GRAM -> new AmountUnit(amount, unit);
            case LITER -> new AmountUnit((Double.valueOf(amount) * 1000), Unit.MILLILITER);
            case OUNCE -> new AmountUnit((Double.valueOf(amount) * 30), Unit.MILLILITER);
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
    }


    final class AmountUnit {
        private final Double amount;
        private final Unit unit;

        /**
         * Used to return standardized unit in {@link #standardizeUnit(Double, Unit)}
         */
        AmountUnit(Double amount, Unit unit) {
            this.amount = amount;
            this.unit = unit;
        }

        public Double getAmount() {
            return amount;
        }

        public Unit getUnit() {
            return unit;
        }
    }
}

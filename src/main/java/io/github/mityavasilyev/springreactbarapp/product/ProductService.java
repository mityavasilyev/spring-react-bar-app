package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.exceptions.NotEnoughProductException;
import io.github.mityavasilyev.springreactbarapp.exceptions.UnitMismatchException;
import io.github.mityavasilyev.springreactbarapp.extra.Ingredient;
import io.github.mityavasilyev.springreactbarapp.extra.Unit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Consumes products that are sources of provided ingredients.
     * If there's some problem with consuming some product - aborts whole process (transactional).
     *
     * @param consumables List of ingredients to consume
     * @return list of affected products after consumption
     * @throws NotEnoughProductException if there's not enough product to consume
     * @throws UnitMismatchException     if there's unit mismatch between product and ingredient
     */
    @Transactional(rollbackFor = {NotEnoughProductException.class})
    public List<Product> consumeIngredients(List<Ingredient> consumables)
            throws NotEnoughProductException, UnitMismatchException {
        List<Product> affectedProducts = new ArrayList<>();
        for (Ingredient ingredient : consumables) {
            affectedProducts.add(consumeOneIngredient(ingredient.getSourceProduct().getId(), ingredient));
        }
        return affectedProducts;
    }

    /**
     * Consumes one product with provided ingredient
     *
     * @param productId  Source product id
     * @param ingredient Ingredient to consume
     * @return Product after consumption
     * @throws NotEnoughProductException
     * @throws UnitMismatchException
     */
    private Product consumeOneIngredient(Long productId, Ingredient ingredient)
            throws NotEnoughProductException, UnitMismatchException {
        // TODO: 06.02.2022 Stop providing ingredient. Use more generic argument instead
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

    /**
     * Standardizes provided amount and unit to use standard values
     *
     * @param amount amount that needs to be standardized
     * @param unit   unit that needs to be standardized
     * @return an AmountUnit that contains standard amount and unit
     */
    private AmountUnit standardizeUnit(Double amount, Unit unit) {
        return switch (unit) {
            case MILLILITER, PIECE, GRAM -> new AmountUnit(amount, unit);
            case LITER -> new AmountUnit((amount * 1000), Unit.MILLILITER);
            case OUNCE -> new AmountUnit((amount * 30), Unit.MILLILITER);
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

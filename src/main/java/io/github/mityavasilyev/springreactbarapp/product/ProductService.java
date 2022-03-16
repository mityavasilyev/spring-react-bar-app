package io.github.mityavasilyev.springreactbarapp.product;

import io.github.mityavasilyev.springreactbarapp.exceptions.DataNotFoundException;
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
@Transactional
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
            throw new DataNotFoundException("No product with such id");
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
        product.setAmountLeft(amountUnit.amount());
        product.setUnit(amountUnit.unit());
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
    @Transactional(rollbackFor = {NotEnoughProductException.class, UnitMismatchException.class})
    public List<Product> consumeIngredients(List<Ingredient> consumables)
            throws NotEnoughProductException, UnitMismatchException {
        List<Product> affectedProducts = new ArrayList<>();
        for (Ingredient ingredient : consumables) {
            affectedProducts.add(
                    consumeOneIngredient(
                            ingredient.getSourceProduct().getId(),
                            ingredient.getAmount(),
                            ingredient.getUnit()
                    ));
        }
        return affectedProducts;
    }

    /**
     * Consumes one product with provided ingredient amount and unit
     *
     * @param productId        Source product id
     * @param ingredientAmount amount of ingredient to consume
     * @param ingredientUnit   unit of ingredient
     * @return Product after consumption
     * @throws NotEnoughProductException not enough product for consumption
     * @throws UnitMismatchException     ingredient unit and source product unit don't match
     */
    private Product consumeOneIngredient(Long productId, Double ingredientAmount, Unit ingredientUnit)
            throws NotEnoughProductException, UnitMismatchException {
        Product consumable = getById(productId);
        AmountUnit productAmountUnit = standardizeUnit(consumable.getAmountLeft(), consumable.getUnit());
        AmountUnit ingredientAmountUnit = standardizeUnit(ingredientAmount, ingredientUnit);
        if (!productAmountUnit.unit().equals(ingredientAmountUnit.unit()))
            throw new UnitMismatchException(
                    String.format(
                            "Can't consume %s because source product is in %s",
                            ingredientAmountUnit.unit(),
                            productAmountUnit.unit()));
        if (productAmountUnit.amount() > 0
                && ((productAmountUnit.amount() - ingredientAmountUnit.amount()) >= 0)) {
            consumable.setAmountLeft(
                    productAmountUnit.amount() - ingredientAmountUnit.amount()
            );
            consumable.setUnit(productAmountUnit.unit());
            productRepository.save(consumable);
            return consumable;
        } else {
            throw new NotEnoughProductException(
                    String.format("Can't consume %s %s of %s (available: %s %s)",
                            ingredientAmount.toString(),
                            ingredientUnit.toString(),
                            consumable.getName(),
                            consumable.getAmountLeft().toString(),
                            consumable.getUnit()
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


    record AmountUnit(Double amount, Unit unit) {
    }
}

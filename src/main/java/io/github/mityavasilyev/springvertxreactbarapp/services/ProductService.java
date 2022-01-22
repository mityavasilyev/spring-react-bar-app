package io.github.mityavasilyev.springvertxreactbarapp.services;

import io.github.mityavasilyev.springvertxreactbarapp.model.Cocktail;
import io.github.mityavasilyev.springvertxreactbarapp.model.Product;
import io.github.mityavasilyev.springvertxreactbarapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * @param name string to search for
     * @return matching products
     */
    public List<Product> getAllByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}

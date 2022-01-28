package io.github.mityavasilyev.springvertxreactbarapp.product;

import io.github.mityavasilyev.springvertxreactbarapp.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Searches repository for products that have Name containing provided string
     *
     * @param name
     * @return matching cocktails
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
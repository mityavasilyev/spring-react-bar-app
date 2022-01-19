package io.github.mityavasilyev.springvertxreactbarapp.repositories;

import io.github.mityavasilyev.springvertxreactbarapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

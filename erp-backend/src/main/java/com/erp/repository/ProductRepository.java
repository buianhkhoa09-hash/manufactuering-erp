package com.erp.repository;

import com.erp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySku(String sku);
    List<Product> findByCategory(String category);
    List<Product> findByStatus(String status);

    @Query("SELECT p FROM Product p WHERE p.stockQty <= p.reorderLevel AND p.status = 'ACTIVE'")
    List<Product> findLowStockProducts();
}

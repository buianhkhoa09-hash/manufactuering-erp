package com.erp.service.impl;

import com.erp.dto.request.ProductRequest;
import com.erp.dto.response.ProductResponse;
import com.erp.model.Product;
import com.erp.model.Supplier;
import com.erp.repository.ProductRepository;
import com.erp.repository.SupplierRepository;
import com.erp.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ProductResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<ProductResponse> findLowStock() {
        return productRepository.findLowStockProducts().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest req) {
        if (productRepository.findBySku(req.getSku()).isPresent())
            throw new IllegalArgumentException("SKU already exists: " + req.getSku());

        Supplier supplier = null;
        if (req.getSupplierId() != null) {
            supplier = supplierRepository.findById(req.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + req.getSupplierId()));
        }

        Product product = Product.builder()
                .name(req.getName())
                .sku(req.getSku())
                .category(req.getCategory())
                .description(req.getDescription())
                .unitPrice(req.getUnitPrice())
                .costPrice(req.getCostPrice())
                .stockQty(req.getStockQty() != null ? req.getStockQty() : 0)
                .reorderLevel(req.getReorderLevel() != null ? req.getReorderLevel() : 10)
                .unit(req.getUnit() != null ? req.getUnit() : "pcs")
                .supplier(supplier)
                .status(req.getStatus() != null ? req.getStatus() : "ACTIVE")
                .build();

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Integer id, ProductRequest req) {
        Product product = getOrThrow(id);

        Supplier supplier = null;
        if (req.getSupplierId() != null) {
            supplier = supplierRepository.findById(req.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + req.getSupplierId()));
        }

        product.setName(req.getName());
        product.setSku(req.getSku());
        product.setCategory(req.getCategory());
        product.setDescription(req.getDescription());
        product.setUnitPrice(req.getUnitPrice());
        product.setCostPrice(req.getCostPrice());
        if (req.getStockQty() != null) product.setStockQty(req.getStockQty());
        if (req.getReorderLevel() != null) product.setReorderLevel(req.getReorderLevel());
        if (req.getUnit() != null) product.setUnit(req.getUnit());
        product.setSupplier(supplier);
        if (req.getStatus() != null) product.setStatus(req.getStatus());

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse adjustStock(Integer id, int delta) {
        Product product = getOrThrow(id);
        int newQty = product.getStockQty() + delta;
        if (newQty < 0) throw new IllegalArgumentException("Stock cannot go below 0");
        product.setStockQty(newQty);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        productRepository.delete(getOrThrow(id));
    }

    private Product getOrThrow(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .sku(p.getSku())
                .category(p.getCategory())
                .description(p.getDescription())
                .unitPrice(p.getUnitPrice())
                .costPrice(p.getCostPrice())
                .stockQty(p.getStockQty())
                .reorderLevel(p.getReorderLevel())
                .unit(p.getUnit())
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .supplierName(p.getSupplier() != null ? p.getSupplier().getName() : null)
                .status(p.getStatus())
                .lowStock(p.getStockQty() <= p.getReorderLevel())
                .createdAt(p.getCreatedAt())
                .build();
    }
}

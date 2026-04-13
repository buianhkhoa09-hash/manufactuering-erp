package com.erp.service.impl;

import com.erp.dto.request.SupplierRequest;
import com.erp.dto.response.SupplierResponse;
import com.erp.model.Supplier;
import com.erp.repository.SupplierRepository;
import com.erp.service.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public List<SupplierResponse> findAll() {
        return supplierRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SupplierResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest req) {
        if (supplierRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already in use: " + req.getEmail());

        Supplier supplier = Supplier.builder()
                .name(req.getName())
                .contactName(req.getContactName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .country(req.getCountry())
                .status(req.getStatus() != null ? req.getStatus() : "ACTIVE")
                .build();

        return toResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public SupplierResponse update(Integer id, SupplierRequest req) {
        Supplier supplier = getOrThrow(id);
        supplier.setName(req.getName());
        supplier.setContactName(req.getContactName());
        supplier.setEmail(req.getEmail());
        supplier.setPhone(req.getPhone());
        supplier.setAddress(req.getAddress());
        supplier.setCountry(req.getCountry());
        if (req.getStatus() != null) supplier.setStatus(req.getStatus());
        return toResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        supplierRepository.delete(getOrThrow(id));
    }

    private Supplier getOrThrow(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + id));
    }

    private SupplierResponse toResponse(Supplier s) {
        return SupplierResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .contactName(s.getContactName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .address(s.getAddress())
                .country(s.getCountry())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .build();
    }
}

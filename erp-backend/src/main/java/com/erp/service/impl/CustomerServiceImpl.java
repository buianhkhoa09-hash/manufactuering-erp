package com.erp.service.impl;

import com.erp.dto.request.CustomerRequest;
import com.erp.dto.response.CustomerResponse;
import com.erp.model.Customer;
import com.erp.repository.CustomerRepository;
import com.erp.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CustomerResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest req) {
        if (customerRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already in use: " + req.getEmail());

        Customer customer = Customer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .city(req.getCity())
                .country(req.getCountry())
                .status(req.getStatus() != null ? req.getStatus() : "ACTIVE")
                .build();

        return toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponse update(Integer id, CustomerRequest req) {
        Customer customer = getOrThrow(id);
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        customer.setPhone(req.getPhone());
        customer.setAddress(req.getAddress());
        customer.setCity(req.getCity());
        customer.setCountry(req.getCountry());
        if (req.getStatus() != null) customer.setStatus(req.getStatus());
        return toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        customerRepository.delete(getOrThrow(id));
    }

    private Customer getOrThrow(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .address(c.getAddress())
                .city(c.getCity())
                .country(c.getCountry())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .build();
    }
}

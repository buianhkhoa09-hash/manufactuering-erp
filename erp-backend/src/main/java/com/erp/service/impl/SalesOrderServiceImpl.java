package com.erp.service.impl;

import com.erp.dto.request.SalesOrderRequest;
import com.erp.dto.response.SalesOrderResponse;
import com.erp.model.*;
import com.erp.repository.*;
import com.erp.service.SalesOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<SalesOrderResponse> findAll() {
        return salesOrderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SalesOrderResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<SalesOrderResponse> findByCustomer(Integer customerId) {
        return salesOrderRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public SalesOrderResponse create(SalesOrderRequest req) {
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + req.getCustomerId()));

        Employee createdBy = null;
        if (req.getCreatedById() != null) {
            createdBy = employeeRepository.findById(req.getCreatedById())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + req.getCreatedById()));
        }

        SalesOrder order = SalesOrder.builder()
                .customer(customer)
                .status(req.getStatus() != null ? req.getStatus() : "DRAFT")
                .orderDate(req.getOrderDate() != null ? req.getOrderDate() : LocalDate.now())
                .deliveryDate(req.getDeliveryDate())
                .notes(req.getNotes())
                .createdBy(createdBy)
                .build();

        List<SalesOrderItem> items = buildItems(req, order);
        order.setItems(items);
        order.setTotalAmount(calcTotal(items));

        return toResponse(salesOrderRepository.save(order));
    }

    @Override
    @Transactional
    public SalesOrderResponse update(Integer id, SalesOrderRequest req) {
        SalesOrder order = getOrThrow(id);

        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + req.getCustomerId()));

        order.setCustomer(customer);
        order.setDeliveryDate(req.getDeliveryDate());
        order.setNotes(req.getNotes());
        if (req.getStatus() != null) order.setStatus(req.getStatus());

        order.getItems().clear();
        List<SalesOrderItem> items = buildItems(req, order);
        order.getItems().addAll(items);
        order.setTotalAmount(calcTotal(items));

        return toResponse(salesOrderRepository.save(order));
    }

    @Override
    @Transactional
    public SalesOrderResponse updateStatus(Integer id, String status) {
        SalesOrder order = getOrThrow(id);
        order.setStatus(status);
        return toResponse(salesOrderRepository.save(order));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        salesOrderRepository.delete(getOrThrow(id));
    }

    // ── Helpers ──────────────────────────────────────────────────

    private List<SalesOrderItem> buildItems(SalesOrderRequest req, SalesOrder order) {
        return req.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemReq.getProductId()));
            return SalesOrderItem.builder()
                    .salesOrder(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .build();
        }).toList();
    }

    private BigDecimal calcTotal(List<SalesOrderItem> items) {
        return items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private SalesOrder getOrThrow(Integer id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found: " + id));
    }

    private SalesOrderResponse toResponse(SalesOrder o) {
        List<SalesOrderResponse.ItemResponse> itemResponses = o.getItems() == null ? List.of() :
                o.getItems().stream().map(i -> SalesOrderResponse.ItemResponse.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .sku(i.getProduct().getSku())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .lineTotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .build()).toList();

        return SalesOrderResponse.builder()
                .id(o.getId())
                .customerId(o.getCustomer().getId())
                .customerName(o.getCustomer().getName())
                .status(o.getStatus())
                .orderDate(o.getOrderDate())
                .deliveryDate(o.getDeliveryDate())
                .totalAmount(o.getTotalAmount())
                .notes(o.getNotes())
                .createdBy(o.getCreatedBy() != null
                        ? o.getCreatedBy().getFirstName() + " " + o.getCreatedBy().getLastName()
                        : null)
                .createdAt(o.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}

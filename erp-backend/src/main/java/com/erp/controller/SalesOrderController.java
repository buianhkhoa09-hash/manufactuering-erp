package com.erp.controller;

import com.erp.dto.request.SalesOrderRequest;
import com.erp.dto.response.SalesOrderResponse;
import com.erp.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @GetMapping
    public ResponseEntity<List<SalesOrderResponse>> getAll() {
        return ResponseEntity.ok(salesOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(salesOrderService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesOrderResponse>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(salesOrderService.findByCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<SalesOrderResponse> create(@Valid @RequestBody SalesOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salesOrderService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderResponse> update(@PathVariable Integer id,
                                                      @Valid @RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(salesOrderService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SalesOrderResponse> updateStatus(@PathVariable Integer id,
                                                            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(salesOrderService.updateStatus(id, body.get("status")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salesOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

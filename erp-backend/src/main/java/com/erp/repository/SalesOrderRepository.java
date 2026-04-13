package com.erp.repository;

import com.erp.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer> {
    List<SalesOrder> findByCustomerId(Integer customerId);
    List<SalesOrder> findByStatus(String status);
    List<SalesOrder> findByCreatedById(Integer employeeId);
}

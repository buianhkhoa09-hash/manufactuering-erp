package com.erp.service;

import com.erp.dto.request.EmployeeRequest;
import com.erp.dto.response.EmployeeResponse;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> findAll();
    EmployeeResponse findById(Integer id);
    List<EmployeeResponse> findByDepartment(Integer departmentId);
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse update(Integer id, EmployeeRequest request);
    EmployeeResponse updateStatus(Integer id, String status);
    void delete(Integer id);
}

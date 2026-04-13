package com.erp.service.impl;

import com.erp.dto.request.EmployeeRequest;
import com.erp.dto.response.EmployeeResponse;
import com.erp.model.Department;
import com.erp.model.Employee;
import com.erp.repository.DepartmentRepository;
import com.erp.repository.EmployeeRepository;
import com.erp.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EmployeeResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<EmployeeResponse> findByDepartment(Integer departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest req) {
        if (employeeRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already in use: " + req.getEmail());

        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + req.getDepartmentId()));

        Employee emp = Employee.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .role(req.getRole())
                .department(dept)
                .hireDate(req.getHireDate())
                .salary(req.getSalary())
                .status(req.getStatus() != null ? req.getStatus() : "ACTIVE")
                .build();

        return toResponse(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public EmployeeResponse update(Integer id, EmployeeRequest req) {
        Employee emp = getOrThrow(id);
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + req.getDepartmentId()));

        emp.setFirstName(req.getFirstName());
        emp.setLastName(req.getLastName());
        emp.setEmail(req.getEmail());
        emp.setPhone(req.getPhone());
        emp.setRole(req.getRole());
        emp.setDepartment(dept);
        emp.setHireDate(req.getHireDate());
        emp.setSalary(req.getSalary());
        if (req.getStatus() != null) emp.setStatus(req.getStatus());

        return toResponse(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public EmployeeResponse updateStatus(Integer id, String status) {
        Employee emp = getOrThrow(id);
        emp.setStatus(status);
        return toResponse(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        employeeRepository.delete(getOrThrow(id));
    }

    private Employee getOrThrow(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
    }

    private EmployeeResponse toResponse(Employee e) {
        return EmployeeResponse.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .role(e.getRole())
                .departmentId(e.getDepartment().getId())
                .departmentName(e.getDepartment().getName())
                .hireDate(e.getHireDate())
                .salary(e.getSalary())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}

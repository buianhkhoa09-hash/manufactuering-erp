package com.erp.service.impl;

import com.erp.dto.request.DepartmentRequest;
import com.erp.dto.response.DepartmentResponse;
import com.erp.model.Department;
import com.erp.repository.DepartmentRepository;
import com.erp.service.DepartmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DepartmentResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName()))
            throw new IllegalArgumentException("Department name already exists: " + request.getName());

        Department dept = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(departmentRepository.save(dept));
    }

    @Override
    @Transactional
    public DepartmentResponse update(Integer id, DepartmentRequest request) {
        Department dept = getOrThrow(id);
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());
        return toResponse(departmentRepository.save(dept));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        departmentRepository.delete(getOrThrow(id));
    }

    private Department getOrThrow(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + id));
    }

    private DepartmentResponse toResponse(Department d) {
        return DepartmentResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .createdAt(d.getCreatedAt())
                .employeeCount(d.getEmployees() == null ? 0 : d.getEmployees().size())
                .build();
    }
}

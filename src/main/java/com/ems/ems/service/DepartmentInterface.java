package com.ems.ems.service;

import com.ems.ems.dto.request.department.DepartmentCreateDto;
import com.ems.ems.dto.request.department.DepartmentUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.department.DepartmentResponseDto;

public interface DepartmentInterface {
    DepartmentResponseDto createDepartment(DepartmentCreateDto newDepartment);
    DepartmentResponseDto getDepartment(Integer id);
    DepartmentResponseDto updateDepartment(Integer id, DepartmentUpdateDto department);
    void removeDepartment(Integer id);
    PaginatedResponse<DepartmentResponseDto> getAllDepartments(Integer page,Integer size,String sortBy,String sortDir);
}

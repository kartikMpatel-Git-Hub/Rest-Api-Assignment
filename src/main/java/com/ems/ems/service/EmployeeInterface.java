package com.ems.ems.service;

import com.ems.ems.dto.request.employee.EmployeeCreateDto;
import com.ems.ems.dto.request.employee.EmployeeUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.employee.EmployeeResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmployeeInterface {

    EmployeeResponseDto createEmployee(EmployeeCreateDto newEmployee);
    PaginatedResponse<EmployeeResponseDto> getAllEmployees(Integer page,Integer size,String sortBy,String sortDir);
    EmployeeResponseDto getEmployee(Integer id);
    void removeEmployee(Integer id);
    EmployeeResponseDto updateDepartment(Integer id, Integer department);
    EmployeeResponseDto updateEmployee(Integer id,EmployeeUpdateDto employee);
    EmployeeResponseDto employeeImage(Integer id, MultipartFile image) throws IOException;

    byte[] getEmployeeImage(Integer id) throws IOException;
}

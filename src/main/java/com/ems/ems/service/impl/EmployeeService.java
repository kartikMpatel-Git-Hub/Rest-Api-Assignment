package com.ems.ems.service.impl;

import com.ems.ems.dto.request.employee.EmployeeCreateDto;
import com.ems.ems.dto.request.employee.EmployeeUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.employee.EmployeeResponseDto;
import com.ems.ems.exception.ResourceNotFoundException;
import com.ems.ems.model.DepartmentModel;
import com.ems.ems.model.EmployeeModel;
import com.ems.ems.repository.EmployeeRepository;
import com.ems.ems.service.EmployeeInterface;
import com.ems.ems.service.FileStorageInterface;
import com.ems.ems.utility.PaginatedResponseCreator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EmployeeService implements EmployeeInterface {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final FileStorageInterface fileStorageInterface;
    private final ModelMapper mapper;
    private final PaginatedResponseCreator responseCreator;

    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto newEmployee) {
        EmployeeModel employee = makeEmployee(newEmployee);
        return mapper.map(employee,EmployeeResponseDto.class);
    }

    @Override
    public PaginatedResponse<EmployeeResponseDto> getAllEmployees(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = responseCreator.getPageable(page, size, sortBy, sortDir);
        Page<EmployeeModel> employees = employeeRepository.findAll(pageable);
        return responseCreator.createPaginatedResponse(employees,EmployeeResponseDto.class);
    }

    @Override
    public EmployeeResponseDto getEmployee(Integer id) {
        return mapper.map(getEmployeeModel(id),EmployeeResponseDto.class);
    }

    @Override
    public void removeEmployee(Integer id) {
        EmployeeModel employee = getEmployeeModel(id);
        employee.setIsDeleted(true);
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponseDto updateDepartment(Integer id, Integer department) {
        EmployeeModel employee = getEmployeeModel(id);
        DepartmentModel departmentModel = departmentService.getDepartmentModel(department);
        employee.setDepartment(departmentModel);
        return mapper.map(employeeRepository.save(employee),EmployeeResponseDto.class);
    }

    @Override
    public EmployeeResponseDto updateEmployee(Integer id, EmployeeUpdateDto employee) {
        EmployeeModel savedEmployee = updateEmployeeDetail(id, employee);
        return mapper.map(savedEmployee,EmployeeResponseDto.class);
    }

    @Override
    public EmployeeResponseDto employeeImage(Integer id, MultipartFile image) throws IOException {
        EmployeeModel employeeModel = getEmployeeModel(id);
        String savedImagePath = fileStorageInterface.storeFile(image);
        employeeModel.setImage(savedImagePath);

        return mapper.map(
                employeeRepository.save(employeeModel),
                EmployeeResponseDto.class);
    }

    @Override
    public byte[] getEmployeeImage(Integer id) throws IOException {
        EmployeeModel employee = getEmployeeModel(id);
        return fileStorageInterface.getImage(employee.getImage());
    }

    private EmployeeModel updateEmployeeDetail(Integer id, EmployeeUpdateDto employee) {
        EmployeeModel existingEmployee = getEmployeeModel(id);
        if(employee.getName() != null)
            existingEmployee.setName(employee.getName());
        if(employee.getEmail() != null)
            existingEmployee.setEmail(employee.getEmail());
        if(employee.getSalary() != null)
            existingEmployee.setSalary(employee.getSalary());
        if (employee.getDepartment() != null
                && !Objects.equals(existingEmployee.getDepartment().getId(),
                employee.getDepartment())){
            DepartmentModel department = departmentService.getDepartmentModel(employee.getDepartment());
            existingEmployee.setDepartment(department);
        }
        return employeeRepository.save(existingEmployee);
    }

    private EmployeeModel makeEmployee(EmployeeCreateDto newEmployee) {
        EmployeeModel employee = new EmployeeModel();
        employee.setName(newEmployee.getName());
        employee.setEmail(newEmployee.getEmail());
        employee.setSalary(newEmployee.getSalary());
        DepartmentModel department = departmentService.getDepartmentModel(newEmployee.getDepartment());
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    protected EmployeeModel getEmployeeModel(Integer id){
        return employeeRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Employee","id",id.toString())
        );
    }
}

package com.ems.ems.controller;

import com.ems.ems.dto.request.employee.EmployeeCreateDto;
import com.ems.ems.dto.request.employee.EmployeeUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.ResponseDto;
import com.ems.ems.dto.response.employee.EmployeeResponseDto;
import com.ems.ems.service.EmployeeInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeInterface employeeInterface;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @RequestBody @Valid EmployeeCreateDto newEmployee
            ){
        return new ResponseEntity<>(
                employeeInterface.createEmployee(newEmployee),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10")Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "asc") String sortDir
    ){
        return new ResponseEntity<>(
                employeeInterface.getAllEmployees(page, size, sortBy, sortDir),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(
                employeeInterface.getEmployee(id),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Integer id,
            @RequestBody @Valid EmployeeUpdateDto employeeUpdate
    ){
        return new ResponseEntity<>(
                employeeInterface.updateEmployee(id,employeeUpdate),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployeeDepartment(
            @PathVariable Integer id,
            @RequestParam(name = "department",defaultValue = "-1")Integer department
    ){
        return new ResponseEntity<>(
                employeeInterface.updateDepartment(id,department),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteEmployee(
            @PathVariable Integer id
    ){
        employeeInterface.removeEmployee(id);
        ResponseDto response = new ResponseDto();
        response.setMessage("Employee With Id : "+id+" Deleted !");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeResponseDto> employeeImage(
            @RequestParam("image")MultipartFile image,
            @PathVariable Integer id
            ) throws IOException {
        return new ResponseEntity<>(
                employeeInterface.employeeImage(id,image),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<MultipartFile> getEmployeeImage(
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(
                employeeInterface.getEmployeeImage(id),
                HttpStatus.OK);
    }

}

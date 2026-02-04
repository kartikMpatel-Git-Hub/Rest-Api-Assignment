package com.ems.ems.controller;

import com.ems.ems.dto.request.employee.EmployeeCreateDto;
import com.ems.ems.dto.request.employee.EmployeeRequestDto;
import com.ems.ems.dto.request.employee.EmployeeUpdateDto;
import com.ems.ems.dto.request.employee.OnUpdate;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.ResponseDto;
import com.ems.ems.dto.response.employee.EmployeeResponseDto;
import com.ems.ems.exception.SomethingWentWrongException;
import com.ems.ems.service.EmployeeInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeInterface employeeInterface;
    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @RequestBody @Valid EmployeeCreateDto newEmployee
            ){
        return new ResponseEntity<>(
                employeeInterface.createEmployee(newEmployee),
                HttpStatus.CREATED);
    }

    @GetMapping(headers = "X-API-VERSION=1")
    public ResponseEntity<PaginatedResponse<EmployeeResponseDto>> getAllEmployeesV1(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10")Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "asc") String sortDir
    ){
        logger.info("get employees version 1");
        return new ResponseEntity<>(
                employeeInterface.getAllEmployees(page, size, sortBy, sortDir),
                HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PaginatedResponse<EmployeeResponseDto>> getAllEmployeesV2(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10")Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "asc") String sortDir
    ){
        logger.info("get employees version Simple");
        return new ResponseEntity<>(
                employeeInterface.getAllEmployees(page, size, sortBy, sortDir),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",params = "version=1")
    public ResponseEntity<EmployeeResponseDto> getEmployee(
            @PathVariable Integer id
    ){
        logger.info("get employee version 1");
        return new ResponseEntity<>(
                employeeInterface.getEmployee(id),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeV2(
            @PathVariable Integer id
    ){
        logger.info("get employee version simple");
        return new ResponseEntity<>(
                employeeInterface.getEmployee(id),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Integer id,
            @Validated(OnUpdate.class)@RequestBody EmployeeRequestDto employeeUpdate
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
    public ResponseEntity<byte[]> getEmployeeImage(
            @PathVariable Integer id
    ) throws IOException {

        var img = employeeInterface.getEmployeeImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getContentType()))
                .header("Content-Disposition", "inline; filename=\"" + img.getFileName() + "\"")
                .body(img.getBytes());

    }

}

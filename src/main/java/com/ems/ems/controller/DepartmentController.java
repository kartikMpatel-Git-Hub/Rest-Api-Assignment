package com.ems.ems.controller;

import com.ems.ems.dto.request.department.DepartmentCreateDto;
import com.ems.ems.dto.request.department.DepartmentUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.ResponseDto;
import com.ems.ems.dto.response.department.DepartmentResponseDto;
import com.ems.ems.service.DepartmentInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentInterface DepartmentInterface;

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @RequestBody @Valid DepartmentCreateDto newDepartment
    ){
        return new ResponseEntity<>(
                DepartmentInterface.createDepartment(newDepartment),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<DepartmentResponseDto>> getAllDepartments(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10")Integer size,
            @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "asc") String sortDir
    ){
        return new ResponseEntity<>(
                DepartmentInterface.getAllDepartments(page, size, sortBy, sortDir),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartment(
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(
                DepartmentInterface.getDepartment(id),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable Integer id,
            @RequestBody @Valid DepartmentUpdateDto departmentUpdate
    ){
        return new ResponseEntity<>(
                DepartmentInterface.updateDepartment(id,departmentUpdate),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteDepartment(
            @PathVariable Integer id
    ){
        DepartmentInterface.removeDepartment(id);
        ResponseDto response = new ResponseDto();
        response.setMessage("Department With Id : "+id+" Deleted !");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }
}

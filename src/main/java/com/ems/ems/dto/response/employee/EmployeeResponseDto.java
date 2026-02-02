package com.ems.ems.dto.response.employee;

import com.ems.ems.dto.response.department.DepartmentResponseDto;
import lombok.Data;

@Data
public class EmployeeResponseDto {
    private Integer id;

    private String name;

    private String email;

    private Double salary;

    private String image;

    private DepartmentResponseDto department;
}

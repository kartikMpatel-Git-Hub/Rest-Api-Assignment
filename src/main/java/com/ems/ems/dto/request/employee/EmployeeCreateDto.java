package com.ems.ems.dto.request.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeCreateDto {

    @NotNull(message = "Employee Name is Required")
    @NotBlank(message = "Employee Name is Empty")
    private String name;

    @NotNull(message = "Employee Email is Required")
    @NotBlank(message = "Employee Email is Empty")
    @Email
    private String email;

    @Min(value = 0,message = "Salary can not be in negative")
    private Double salary;

    @Min(value = 0,message = "Department can't be in negative")
    private Integer department;
}

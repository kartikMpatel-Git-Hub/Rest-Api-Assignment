package com.ems.ems.dto.request.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequestDto {

    @NotNull(message = "Employee Name is Required",groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Employee Name is Empty",groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Employee Email is Required",groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Employee Email is Empty",groups = {OnCreate.class, OnUpdate.class})
    @Email
    private String email;

    @Min(value = 0,message = "Salary can not be in negative",groups = {OnCreate.class, OnUpdate.class})
    private Double salary;

    @Min(value = 0,message = "Department can't be in negative",groups = {OnCreate.class, OnUpdate.class})
    private Integer department;
}

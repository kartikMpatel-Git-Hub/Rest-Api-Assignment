package com.ems.ems.dto.request.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentCreateDto {
    @NotNull(message = "department Name is Required")
    @NotBlank(message = "department Name is Empty")
    private String department;
}

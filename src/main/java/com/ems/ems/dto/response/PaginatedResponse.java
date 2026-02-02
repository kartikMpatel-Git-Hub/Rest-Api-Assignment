package com.ems.ems.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> content;
    private Integer totalPage;
    private Long totalItems;
    private Boolean isLast;
}

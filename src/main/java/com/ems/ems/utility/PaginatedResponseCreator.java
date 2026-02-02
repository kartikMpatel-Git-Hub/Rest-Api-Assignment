package com.ems.ems.utility;


import com.ems.ems.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaginatedResponseCreator {

    private final ModelMapper modelMapper;

    public <T,S> PaginatedResponse<T> createPaginatedResponse(Page<S> data, Class<T> to){
        PaginatedResponse<T> response = new PaginatedResponse<>();
        response.setContent(getContent(data,to));
        response.setTotalPage(data.getTotalPages());
        response.setTotalItems(data.getTotalElements());
        response.setIsLast(data.isLast());
        return response;
    }

    private <T, S> List<T> getContent(Page<S> data, Class<T> to) {
        return data.stream().map((d)->modelMapper.map(d,to)).toList();
    }


    public Pageable getPageable(Integer page, Integer size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);
    }
}

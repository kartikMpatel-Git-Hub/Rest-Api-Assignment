package com.ems.ems.service.impl;

import com.ems.ems.dto.request.department.DepartmentCreateDto;
import com.ems.ems.dto.request.department.DepartmentUpdateDto;
import com.ems.ems.dto.response.PaginatedResponse;
import com.ems.ems.dto.response.department.DepartmentResponseDto;
import com.ems.ems.exception.ResourceNotFoundException;
import com.ems.ems.model.DepartmentModel;
import com.ems.ems.repository.DepartmentRepository;
import com.ems.ems.service.DepartmentInterface;
import com.ems.ems.utility.PaginatedResponseCreator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepartmentService implements DepartmentInterface {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;
    private final PaginatedResponseCreator responseCreator;

    @Override
    public DepartmentResponseDto createDepartment(DepartmentCreateDto newDepartment) {
        DepartmentModel savedDepartment = makeDepartment(newDepartment);
        return mapper.map(savedDepartment,DepartmentResponseDto.class);
    }



    @Override
    public DepartmentResponseDto getDepartment(Integer id) {
        return mapper.map(getDepartmentModel(id),DepartmentResponseDto.class);
    }

    @Override
    public DepartmentResponseDto updateDepartment(Integer id, DepartmentUpdateDto department) {
        DepartmentModel updatedDepartment = updateDepartmentDetail(id,department);
        return mapper.map(updatedDepartment,DepartmentResponseDto.class);
    }



    @Override
    public void removeDepartment(Integer id) {
        DepartmentModel department = getDepartmentModel(id);
        department.setIsDeleted(true);
        departmentRepository.save(department);
    }

    @Override
    public PaginatedResponse<DepartmentResponseDto> getAllDepartments(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = responseCreator.getPageable(page, size, sortBy, sortDir);
        Page<DepartmentModel> departments = departmentRepository.findAll(pageable);
        return responseCreator.createPaginatedResponse(departments, DepartmentResponseDto.class);
    }

    private DepartmentModel updateDepartmentDetail(Integer id, DepartmentUpdateDto department) {
        DepartmentModel existingDepartment = getDepartmentModel(id);
        if (department.getDepartment() != null)
            existingDepartment.setDepartment(department.getDepartment());
        return departmentRepository.save(existingDepartment);
    }

    private DepartmentModel makeDepartment(DepartmentCreateDto newDepartment) {
        DepartmentModel department = new DepartmentModel();
        department.setDepartment(newDepartment.getDepartment());
        return departmentRepository.save(department);
    }

    protected DepartmentModel getDepartmentModel(Integer id){
        return departmentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Department","id",id.toString())
        );
    }
}

package com.ems.ems.repository;

import com.ems.ems.model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel,Integer> {
    Optional<EmployeeModel> findByEmail(String email);
}

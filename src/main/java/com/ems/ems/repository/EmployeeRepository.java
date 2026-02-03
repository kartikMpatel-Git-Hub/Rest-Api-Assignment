package com.ems.ems.repository;

import com.ems.ems.model.EmployeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel,Integer> {
    @Query("""
    select e
    from EmployeeModel e
    where e.isDeleted = false
        and e.id = :id
        """)
    Optional<EmployeeModel> findById(Integer id);

    @Query("""
    select e
    from EmployeeModel e
    where e.isDeleted = false
    """)
    Page<EmployeeModel> findAll(Pageable pageable);
}

package com.ems.ems.repository;

import com.ems.ems.model.DepartmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentModel,Integer> {

    @Query("""
    select d
    from DepartmentModel d
    where d.isDeleted = false
        and d.id = :id
        """)
    Optional<DepartmentModel> findById(Integer id);

    @Query("""
    select d
    from DepartmentModel d
    where d.isDeleted = false 
    """)
    Page<DepartmentModel> findAll(Pageable pageable);
}

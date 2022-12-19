package com.miniproject.demo.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.miniproject.demo.entity.Student;



@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

	
	@Query("SELECT COUNT(u) FROM Student u WHERE u.standard =:n")
	Integer countByStandard(@Param("n") String standard);

}

package com.miniproject.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniproject.demo.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
	
	Faculty findByNameAndGenderAndContactNumber(String name, String gender, String contactNumber);
}

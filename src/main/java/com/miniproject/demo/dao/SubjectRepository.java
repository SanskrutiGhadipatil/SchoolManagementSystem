package com.miniproject.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.miniproject.demo.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	Subject findBySubjectNameAndStandardAllocatedAndTimeDuration(String subjectName, String standardAllocated,
			String timeDuration);
	
	@Query("Select u.standardAllocated from Subject u GROUP BY u.standardAllocated HAVING COUNT(u)>=6")
	List<String> SubjectAllocatedToStandardCheck();
	
	@Query("Select DISTINCT u.subjectName,u.standardAllocated from Subject u")
	//@Query("Select v.standardAllocated from (Select DISTINCT u.subjectName,u.standardAllocated from Subject u) v GROUP BY v.standardAllocated HAVING COUNT(u)>=6")
	List<String> newSubjectAllocatedToStandardCheck();
	
	

}

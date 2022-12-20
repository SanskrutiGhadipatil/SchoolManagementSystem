package com.miniproject.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.service.FacultyService;
import com.miniproject.demo.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService{
	
	
	@Autowired
	private FacultyService facultyService;
	
	//Used to validate the faculty within the subject class.
	public void validateSubject(Student student) {
		try {
		for(Subject s:student.getSubject()) {
			facultyService.validateFaculty(s);
		}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	

}

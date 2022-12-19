package com.miniproject.demo.service;

import java.util.List;

import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.FacultyNotFoundException;

public interface FacultyService {
	
	public Faculty addFaculty(Faculty faculty);
	
	public List<Faculty> getFaculty();
	
	public void updateFaculty(Faculty faculty, int id) throws FacultyNotFoundException;
	
	public void deleteFaculty(int id) throws FacultyNotFoundException;
	
	public void validateFaculty(Student student);
	
	public void validateFaculty(Subject sub);
}

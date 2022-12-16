package com.miniproject.demo.service;

import java.util.List;

import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.exception.FacultyNotFoundException;

public interface FacultyService {
	
	public void addFaculty(Faculty faculty);
	
	public List<Faculty> getFaculty();
	
	public void updateFaculty(Faculty faculty, int id) throws FacultyNotFoundException;
	
	public void deleteFaculty(int id) throws FacultyNotFoundException;
}

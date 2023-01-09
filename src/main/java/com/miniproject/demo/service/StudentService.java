package com.miniproject.demo.service;

import java.util.List;

import com.miniproject.demo.entity.Student;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;

public interface StudentService {
	
	public Student enrollNewStudent(Student student) throws ClassCapacityFullException, SubjectNotAllocatedToStandardException;
	
	public List<Student> getStudents();
	
	public Student updateStudent(Student student, int id) throws StudentNotFoundException;
	
	public void deleteStudent(int id) throws StudentNotFoundException;
	
}

package com.miniproject.demo.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.service.StudentService;

import jakarta.validation.Valid;

@RestController
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	
	//Adding new Students
	@PostMapping("/enrollnewstudent")
	public ResponseEntity<String> enrollNewStudent(@RequestBody @Valid Student student) throws ClassCapacityFullException, SubjectNotAllocatedToStandardException {
		if(Objects.nonNull(student))
		{
		studentService.enrollNewStudent(student); // handle null
		return new ResponseEntity<>("Student Details Saved",HttpStatus.CREATED);
		}else {
		return new ResponseEntity<>("No Data Found",HttpStatus.NOT_FOUND);
		}
			
			
	}
	
	
	//Reading from Students
	@GetMapping("/get/students")
	public List<Student> getStudents() {
		return studentService.getStudents();
	}
	
	//Updating Student details
	@PutMapping("/update/student/{id}")
	public ResponseEntity<String> updateStudent(@RequestBody Student student ,@PathVariable("id") int id) throws StudentNotFoundException {
		
		studentService.updateStudent(student, id);
		return ResponseEntity.ok("Student data Updated");
	
		
	}
	
	//Deleting Student Details
	@DeleteMapping("/delete/student/{id}")
	public ResponseEntity<String> deleteStudent(@PathVariable("id") int id) throws StudentNotFoundException{
		studentService.deleteStudent(id);
		return ResponseEntity.ok("Student Deleted");
	}
			

}

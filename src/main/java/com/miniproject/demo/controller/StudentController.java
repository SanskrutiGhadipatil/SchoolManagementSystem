package com.miniproject.demo.controller;

import java.util.List;
import java.util.Optional;

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
	public String enrollNewStudent(@RequestBody @Valid Student student) throws ClassCapacityFullException, SubjectNotAllocatedToStandardException {

		Student s=null;
			s=this.studentService.enrollNewStudent(student);
			if(null==s) {
				return "No Student was added";
			}
			return "Student Details Saved";					
	}
	
	
	//Reading from Students, returns list of students
	@GetMapping("/students")
	public  ResponseEntity<List<Student>> getStudents() {
		
		try {
		List<Student> li= studentService.getStudents();
		if(li.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(li));
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	
	//Updating Student details
	@PutMapping("/student/{id}")
	public ResponseEntity<String> updateStudent(@RequestBody Student student ,@PathVariable("id") int id) {	
		try {
		studentService.updateStudent(student, id);
		return ResponseEntity.ok("Student data Updated");
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	
		
	}
	
	//Deleting Student Details
	@DeleteMapping("/student/{id}")
	public ResponseEntity<String> deleteStudent(@PathVariable("id") int id) throws StudentNotFoundException{
		try {
		studentService.deleteStudent(id);
		return ResponseEntity.ok("Student Deleted");
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
			

}

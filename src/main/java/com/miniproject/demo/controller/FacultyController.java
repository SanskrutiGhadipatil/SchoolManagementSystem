package com.miniproject.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.exception.FacultyNotFoundException;
import com.miniproject.demo.service.FacultyService;

@RestController
public class FacultyController {
	
	@Autowired
	private FacultyService facultyService;
	
	//Adding new Faculties
		@PostMapping("/addfaculty")     //seperate controller
		public String addFaculty(@RequestBody Faculty faculty) {
			facultyService.addFaculty(faculty);
			return "Faculty Added";
		}
		
		//Reading From Faculties
		@GetMapping("/get/faculty")
		public List<Faculty> getFaculty(){
			return facultyService.getFaculty();
		}
		
		//Updating Faculty Details
		@PutMapping("/update/faculty/{id}")
		public ResponseEntity<String> updateFaculty(@RequestBody Faculty faculty, @PathVariable("id") int id) throws FacultyNotFoundException{
			facultyService.updateFaculty(faculty, id);
			return ResponseEntity.ok("Faculty Details Updated");	
		}
		
		//Deleting Faculty Details
		@DeleteMapping("/delete/faculty/{id}")
		public ResponseEntity<String> deleteFaculty(@PathVariable("id") int id) throws FacultyNotFoundException{
			facultyService.deleteFaculty(id);
			return ResponseEntity.ok("Faculty Deleted");
		}
		

}

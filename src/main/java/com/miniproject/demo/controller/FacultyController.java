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
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.service.FacultyService;

@RestController
public class FacultyController {
	
	@Autowired
	private FacultyService facultyService;
	
	//Adding new Faculties
		@PostMapping("/addfaculty")     
		public ResponseEntity<String> addFaculty(@RequestBody Faculty faculty) {
			Faculty f=null;
			try {
			f=facultyService.addFaculty(faculty);
			if(f==null) {
				throw new NullPointerException();
			}
			return new ResponseEntity<>("Faculty Details Saved",HttpStatus.CREATED);
			}catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
			}
		}
		
		//Reading From Faculties
		@GetMapping("/faculty")
		public ResponseEntity<List<Faculty>> getFaculty(){
			List<Faculty> li=facultyService.getFaculty();
			if(li.size()<=0)
			{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.of(Optional.of(li));
			
		}
		
		//Updating Faculty Details
		@PutMapping("/faculty/{id}")
		public ResponseEntity<String> updateFaculty(@RequestBody Faculty faculty, @PathVariable("id") int id) {
			try {
			facultyService.updateFaculty(faculty, id);
			return ResponseEntity.ok("Faculty Details Updated");	
			}catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
			}
		}
		
		//Deleting Faculty Details
		@DeleteMapping("/faculty/{id}")
		public ResponseEntity<String> deleteFaculty(@PathVariable("id") int id) {
			try {
				facultyService.deleteFaculty(id);
				return ResponseEntity.ok("Faculty Deleted");
			}catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
			}
			
		}
		

}

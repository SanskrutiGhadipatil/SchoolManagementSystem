package com.miniproject.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miniproject.demo.dao.FacultyRepository;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.FacultyNotFoundException;
import com.miniproject.demo.service.AddressService;
import com.miniproject.demo.service.FacultyService;

@Service
public class FacultyServiceImpl implements FacultyService {

	@Autowired
	private FacultyRepository repository;
	
	
	@Autowired
	private AddressService addressService;
	
	//Adding the faculty to database, returns the faculty added
	@Override
	public Faculty addFaculty(Faculty faculty) {
		 addressService.validateAddress(faculty);     
	     return repository.save(faculty); 
	     
	}

	//returns list of faculties
	@Override
	public List<Faculty> getFaculty() {
		try {
		List<Faculty> list=repository.findAll();   
		return list;
		}catch (Exception e) {
			throw e;
		}
	}

	//updating the faculty details, returns the updated faculty
	@Override
	public void updateFaculty(Faculty faculty, int id) throws FacultyNotFoundException {
		Optional<Faculty> f=repository.findById(id);
		if(f.isPresent()) {
			Faculty f1=f.get();
			f1.setAddress(faculty.getAddress());
			f1.setContactNumber(faculty.getContactNumber());
			f1.setDepartmentId(faculty.getDepartmentId());
			f1.setGender(faculty.getGender());
			f1.setName(faculty.getName());
			f1.setStandardAllocated(faculty.getStandardAllocated());
			repository.save(f1);
		}
		else {
			throw new FacultyNotFoundException("Faculty Details not Found");
		}
		
	}

	//deleting faculty from database
	@Override
	public void deleteFaculty(int id) throws FacultyNotFoundException {
		Optional<Faculty> faculty=repository.findById(id);
		if(faculty.isPresent()) {
			repository.delete(faculty.get());
		}
		else {
			throw new FacultyNotFoundException("Faculty you are trying to delete Does not exsist");
		}
		
	}
	
	//checking if faculty already present in database
	public void validateFaculty(Student student) {
		try {
		Faculty faculty=repository.findByNameAndGenderAndContactNumber(student.getFacultyAllocated().getName(),student.getFacultyAllocated().getGender(),student.getFacultyAllocated().getContactNumber());
			student.setFacultyAllocated(faculty);			     
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//checking if faculty already present in database
	public void validateFaculty(Subject sub) {
		try {
		Faculty faculty=repository.findByNameAndGenderAndContactNumber(sub.getFacultyAllocated().getName(),sub.getFacultyAllocated().getGender(),sub.getFacultyAllocated().getContactNumber());
			sub.setFacultyAllocated(faculty);			     
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

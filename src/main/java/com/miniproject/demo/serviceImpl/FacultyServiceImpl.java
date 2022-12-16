package com.miniproject.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproject.demo.dao.AddressRepository;
import com.miniproject.demo.dao.FacultyRepository;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.exception.FacultyNotFoundException;
import com.miniproject.demo.service.FacultyService;

@Service
public class FacultyServiceImpl implements FacultyService {

	@Autowired
	private FacultyRepository repository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public void addFaculty(Faculty faculty) {
		 validateAddress(faculty);
	     repository.save(faculty);
	}

	@Override
	public List<Faculty> getFaculty() {
		List<Faculty> list=repository.findAll();
		return list;
	}

	@Override
	public void updateFaculty(Faculty faculty, int id) throws FacultyNotFoundException {
		Optional<Faculty> f1=repository.findById(id);
		if(f1.isPresent()) {
			f1.get().setAddress(faculty.getAddress());
			f1.get().setContactNumber(faculty.getContactNumber());
			f1.get().setDepartmentId(faculty.getDepartmentId());
			f1.get().setGender(faculty.getGender());
			f1.get().setName(faculty.getName());
			f1.get().setStandardAllocated(faculty.getStandardAllocated());
			repository.save(f1.get());
		}
		else {
			throw new FacultyNotFoundException("Faculty Details not Found");
		}
		
	}

	@Override
	public void deleteFaculty(int id) throws FacultyNotFoundException {
		Optional<Faculty> faculty=repository.findById(id);
		if(faculty.isPresent()) {
			try {
			repository.delete(faculty.get());
			}
			catch(Exception ex){
				throw new FacultyNotFoundException("Referenced Faculty cant be deleted");
			}
		}
		else {
			throw new FacultyNotFoundException("Faculty you are trying to delete Does not exsist");
		}
		
	}
	
	public void validateAddress(Faculty faculty) {
		Address a=faculty.getAddress();
		Address address=addressRepository.findByCityAndAddressLine1AndAddressLine2AndPinCodeAndCountry(a.getCity(),a.getAddressLine1(),a.getAddressLine2(),a.getPinCode(),a.getCountry());
		if(address!=null) {
			faculty.setAddress(address);
		}	
	}

}

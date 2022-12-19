package com.miniproject.demo.service;

import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;

public interface AddressService {
	
	public void validateAddress(Student student);
	
	public void validateAddress(Faculty faculty);

}

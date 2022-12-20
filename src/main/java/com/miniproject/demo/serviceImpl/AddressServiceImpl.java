package com.miniproject.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproject.demo.dao.AddressRepository;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
    //Method is used check if address is already present in database
	@Override
	public void validateAddress(Student student) {
		try {
			Address a=student.getAddress();
			Address address=addressRepository.findByCityAndAddressLine1AndAddressLine2AndPinCodeAndCountry(a.getCity(),a.getAddressLine1(),a.getAddressLine2(),a.getPinCode(),a.getCountry());
			if(address!=null) {
				student.setAddress(address);
			}	
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	
	//Method is used check if address is already present in database
	public void validateAddress(Faculty faculty) {
		try {
			Address a=faculty.getAddress();
			Address address=addressRepository.findByCityAndAddressLine1AndAddressLine2AndPinCodeAndCountry(a.getCity(),a.getAddressLine1(),a.getAddressLine2(),a.getPinCode(),a.getCountry());
			if(address!=null) {
				faculty.setAddress(address);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
			
	}

}

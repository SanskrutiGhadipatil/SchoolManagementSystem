package com.miniproject.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniproject.demo.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

	Address findByCityAndAddressLine1AndAddressLine2AndPinCodeAndCountry(String city, String addressLine1,
			String addressLine2, String pinCode, String country);

}

package com.miniproject.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Faculty")
public class Faculty {
	
	//: name, gender, facultyId, address, departmentId, standard/class Allotted, contactNumber.
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int facultyId;
	@NotBlank
	private String name;
	private String gender;
	@OneToOne(cascade = {CascadeType.ALL})
	private Address address;
	private int departmentId;
	private String standardAllocated;
	@NotBlank
	@Pattern(regexp = "^[0-9]{10}", message = "Invalid number! PLease enter valid phone number")
	private String contactNumber;
	
	public int getFacultyId() {
		return facultyId;
	}
	public void setFacultyId(int facultyId) {
		this.facultyId = facultyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getStandardAllocated() {
		return standardAllocated;
	}
	public void setStandardAllocated(String standardAllocated) {
		this.standardAllocated = standardAllocated;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public Faculty(int facultyId, String name, String gender, Address address, int departmentId,
			String standardAllocated, String contactNumber) {
		super();
		this.facultyId = facultyId;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.departmentId = departmentId;
		this.standardAllocated = standardAllocated;
		this.contactNumber = contactNumber;
	}
	public Faculty() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	

}

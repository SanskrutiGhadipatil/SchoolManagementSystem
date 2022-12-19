package com.miniproject.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Student")
public class Student {
	//studentName, gender, studentId, address, standard (example: I, II, IV), classTeacherId, 
	//emergencyContactNumber, subjects allotted, facultyAllotted to corresponding subject
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int studentId;
	@NotBlank
	private String studentName;
	private String gender;
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	private String standard;
	private String classTeacherId;
	private String emergencyContactNumber;
	@OneToMany(targetEntity = Subject.class,cascade = {CascadeType.ALL})
	@JoinColumn(name="st_fk",referencedColumnName = "studentId")
	private List<Subject> subject;
	@OneToOne(cascade = {CascadeType.ALL})
	private Faculty facultyAllocated;
	
	public Student(String studentName, String gender, int studentId, Address address, String standard,
			String classTeacherId, String emergencyContactNumber, List<Subject> subject, Faculty facultyAllocated) {
		super();
		this.studentName = studentName;
		this.gender = gender;
		this.studentId = studentId;
		this.address = address;
		this.standard = standard;
		this.classTeacherId = classTeacherId;
		this.emergencyContactNumber = emergencyContactNumber;
		this.subject = subject;
		this.facultyAllocated = facultyAllocated;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getClassTeacherId() {
		return classTeacherId;
	}

	public void setClassTeacherId(String classTeacherId) {
		this.classTeacherId = classTeacherId;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public List<Subject> getSubject() {
		return subject;
	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}

	public Faculty getFacultyAllocated() {
		return facultyAllocated;
	}

	public void setFacultyAllocated(Faculty facultyAllocated) {
		this.facultyAllocated = facultyAllocated;
	}
	
}

package com.miniproject.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Subject")
public class Subject {
	//  subjectName, facultyAllotted, standardAllotted, timeDuration
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String subjectName;	
	@OneToOne(cascade = {CascadeType.ALL})
	private Faculty facultyAllocated;
	private String standardAllocated;
	@Max(value = 45)
	private int timeDuration;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getsubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Faculty getFacultyAllocated() {
		return facultyAllocated;
	}
	public void setFacultyAllocated(Faculty facultyAllocated) {
		this.facultyAllocated = facultyAllocated;
	}
	public String getStandardAllocated() {
		return standardAllocated;
	}
	public void setStandardAllocated(String standardAllocated) {
		this.standardAllocated = standardAllocated;
	}
	public int getTimeDuration() {
		return timeDuration;
	}
	public void setTimeDuration(int timeDuration) {
		this.timeDuration = timeDuration;
	}
	
	public Subject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Subject(int id, String subjectName, Faculty facultyAllocated, String standardAllocated,
			int timeDuration) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.facultyAllocated = facultyAllocated;
		this.standardAllocated = standardAllocated;
		this.timeDuration = timeDuration;
	}
	

}

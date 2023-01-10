package com.miniproject.demo.entity;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;


//import javax.xml.validation.Validator;


class StudentTest {

	
	Student s1=new Student();
	
	@BeforeEach
	void init() {
	
		Address address=new Address(1,"Pune","nagar","Sheri","India", "411014");
		Faculty faculty=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
		Subject subject=new Subject(1,"Maths",faculty,"V",45);
		List<Subject> subjectList=new ArrayList<>();
		
		subjectList.add(subject);
		s1.setStudentName("Sanskruti");
		s1.setClassTeacherId("1");
		s1.setEmergencyContactNumber("2341578374");
		s1.setGender("Female");
		s1.setStandard("V");
		s1.setStudentId(1);
		s1.setAddress(address);
		s1.setFacultyAllocated(faculty);
		s1.setSubject(subjectList);
	}
	
	@Test
	void test_student_noVoilation() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Student>> constraintViolations =
                validator.validate(s1);
		assertThat(constraintViolations.size()).isZero();
	}
	
	@Test
	void test_student_Voilation() {
		s1.setStudentName(null);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Student>> constraintViolations =
                validator.validate(s1);
		
		assertThat(constraintViolations.size()).isOne();
	}

}

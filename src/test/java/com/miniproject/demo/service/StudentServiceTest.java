package com.miniproject.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.miniproject.demo.dao.StudentRepository;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.serviceImpl.StudentServiceImpl;

class StudentServiceTest {

	@Mock
	private StudentRepository repository;
	
	@Mock
	private AddressService addressService;
	
	@Mock
	private FacultyService facultyService;
	
	@Mock
	private SubjectService subjectService;
	
	
	@InjectMocks
	private StudentServiceImpl service;
	
	Student s1=new Student();
	Address address=new Address(1,"Pune","nagar","Sheri","India", "411014");
	Faculty faculty=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
	Subject subject=new Subject(1,"Maths",faculty,"V",45);
	List<Subject> subjectList=new ArrayList<>();
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		

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
	
	
	@DisplayName("To get All Students")
	@Test
	void TestGetStudents() {
		
		List<Student> students=new ArrayList<>();
		students.add(s1);
	
		Mockito.when(repository.findAll()).thenReturn(students);
		List<Student> studentExpected=service.getStudents();
		assertEquals(studentExpected, students);
		assertEquals(studentExpected.get(0).getStudentName(), "Sanskruti");
		
	}
	
	@DisplayName("Get all students returning Empty list")
	@Test
	void TestGetStudent_failed() {
		List<Student> students=new ArrayList<>();
		Mockito.when(repository.findAll()).thenReturn(students);
		List<Student> studentExpected=service.getStudents();
		assertEquals(studentExpected, students);		
		
	}
	
	@DisplayName("Save Student_success")
	@Test
	void testEnrollNewStudent() throws ClassCapacityFullException, SubjectNotAllocatedToStandardException {
		Mockito.doNothing().when(addressService).validateAddress(s1);
		Mockito.doNothing().when(facultyService).validateFaculty(s1);
		Mockito.doNothing().when(subjectService).validateSubject(s1);
		Mockito.when(repository.save(Mockito.any(Student.class))).thenReturn(s1);
		Student ExpectedStudent=service.enrollNewStudent(s1);
		assertThat(ExpectedStudent).isNotNull();
		Mockito.verify(repository).save(Mockito.any(Student.class));
		
	}
	
	@DisplayName("Save Student_capacityFull")
	@Test
	void testEnrollNewStudent_capacityFull() {
		
		StudentServiceImpl spyStud=Mockito.spy(service);
		Mockito.doReturn(false).when(spyStud).checkStaticMap(Mockito.any());
		ClassCapacityFullException ex= assertThrows(ClassCapacityFullException.class, ()->spyStud.enrollNewStudent(s1));
		assertEquals("The Standard you are trying to admit student is full", ex.getMessage());
	}
	
	@DisplayName("Save Student_SubjectNotAllocatedToStandard")
	@Test
	void testEnrollNewStudent_SubjectNotAllocatedToStandard() {
		StudentServiceImpl spyStud=Mockito.spy(service);
		Mockito.doReturn(1).when(spyStud).subjectAllocatedToStandardCheck(Mockito.any(), Mockito.any());
		assertThrows(SubjectNotAllocatedToStandardException.class, ()->spyStud.enrollNewStudent(s1));
	}
	
	@DisplayName("Update Student Details")
	@Test
	void updateStudent_Success() throws StudentNotFoundException {
		Mockito.when(repository.findById(s1.getStudentId())).thenReturn(Optional.of(s1));
		Mockito.when(repository.save(Mockito.any(Student.class))).thenReturn(s1);
		Student  updatedStudent = service.updateStudent(s1, s1.getStudentId());
		assertThat(updatedStudent).isNotNull();				
	}
	
	@DisplayName("Update Student Details when student not found")
	@Test
	void updateStudent_StudentNotFoundException() throws StudentNotFoundException {
		Mockito.when(repository.findById(s1.getStudentId())).thenReturn(Optional.empty());
		StudentNotFoundException ex=assertThrows(StudentNotFoundException.class,()-> service.updateStudent(s1, s1.getStudentId()));
		assertEquals("Student is not present in Database",ex.getMessage());
	}
	
	@DisplayName("Delete Student data")
	@Test
	void testDeleteStudent() throws StudentNotFoundException {
		//Mockito.doNothing().when(repository).delete(s1);
		Mockito.when(repository.findById(s1.getStudentId())).thenReturn(Optional.of(s1));
		service.deleteStudent(s1.getStudentId());
		service.deleteStudent(s1.getStudentId());
		Mockito.verify(repository, Mockito.times(2)).delete(s1);
	}
	
	@DisplayName("Delete Student data when student not found")
	@Test
	void testDeleteStudentData_StudentNotFound() {
		Mockito.when(repository.findById(s1.getStudentId())).thenReturn(Optional.empty());
		StudentNotFoundException ex= assertThrows(StudentNotFoundException.class,()-> service.deleteStudent(s1.getStudentId()));
		assertEquals("Student Does not exsist",ex.getMessage());
	}
	
	

}

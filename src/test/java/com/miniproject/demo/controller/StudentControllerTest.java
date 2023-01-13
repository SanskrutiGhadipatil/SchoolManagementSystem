package com.miniproject.demo.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.miniproject.demo.advice.ApplicationExceptionHandler;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.serviceImpl.StudentServiceImpl;

@ExtendWith(SpringExtension.class)            //used to enable spring support
class StudentControllerTest {

	@InjectMocks
	private StudentController controller;
	
	@Mock
	private StudentServiceImpl service;
	
	//Spring Boot test tool class that lets you test controllers without needing to start an HTTP server
	private MockMvc mockMvc;
	
	//ObjectMapper provides functionality for reading and writing JSON, 
	//either to and from basic POJOs (Plain Old Java Objects)
	private final ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter=objectMapper.writer();
		
	Student s1=new Student();
	Address address=new Address(1,"Pune","nagar","Sheri","India", "411014");
	Faculty faculty=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
	Subject subject=new Subject(1,"Maths",faculty,"V",45);
	List<Subject> subjectList=new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		//MockMvcBuilders.standaloneSetup allows to test a single controller at a time,
		//building the DispatcherServlet and all the necessary infrastructure behind.
		this.mockMvc=MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new ApplicationExceptionHandler())
				.build();
		
	}
	@BeforeEach
	public void init() {
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
	void testGetAllStudents() throws Exception {
		List<Student> students=new ArrayList<>();
		students.add(s1);
		
		Mockito.when(service.getStudents()).thenReturn(students);
		//MockMvc allows to specify the type of request we want to send and the response we expect.
		//Perform a request and return a type that allows chaining further actions, such as asserting expectations, on the result.
		mockMvc.perform(MockMvcRequestBuilders              //MockMvcRequestBuilders is used to build a request
				.get("/students")
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.size()", is(students.size())))  //jsonPath extracts the response content and provides the requested value.
		        .andExpect(jsonPath("$[0].studentName", is("Sanskruti")));
	}
	
	@DisplayName("To get All Students when list is Empty")
	@Test
	void testGetAllStudents_ListEmpty() throws Exception {
		List<Student> students=new ArrayList<>();
		Mockito.when(service.getStudents()).thenReturn(students);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/students")
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
	}
	
	@DisplayName("Enroll new Student_Success")
	@Test
	void testEnrollNewStudent() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);    //converting object to json string
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 MvcResult mvcResult= mockMvc.perform(mockRequest)
	             .andExpect(status().isOk())
	             .andReturn();
		 
		 String actualResponseBody = mvcResult.getResponse().getContentAsString();
		 
		 String expected="Student Details Saved";
		 
		 assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expected);		   	
	}
	
	@DisplayName("Enroll new Student_ student not added")
	@Test
	void testEnrollNewStudent_notAdded() throws Exception{
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(null);
		
	    String content=objectWriter.writeValueAsString(s1);
		 
		MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		MvcResult mvcResult=mockMvc.perform(mockRequest)
	             .andExpect(status().isOk())
	             .andReturn();
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		 
		 String expected="No Student was added";
		 
		 assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expected);
			
		
	}
	
	@DisplayName("Enroll new Student_CapacityFull")
	@Test
	void testEnrollNewStudent_CapacityFull() throws Exception  {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenThrow(ClassCapacityFullException.class);
		
		String content=objectWriter.writeValueAsString(s1);
		 
		MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());
		
	}
	
	@DisplayName("Enroll new Student_StudentNotAllocatedToStandard")
	@Test
	void testEnrollNewStudent_StudentNotAllocatedToStandard() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenThrow(SubjectNotAllocatedToStandardException.class);
		
		String content=objectWriter.writeValueAsString(s1);
		 
		MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());	
	}
	
	
	@Test
	void testDeleteStudent() throws Exception {
		Mockito.doNothing().when(service).deleteStudent(s1.getStudentId());
		MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
				.delete("/student/{id}",s1.getStudentId())
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andReturn();
		
         String actualResponseBody = mvcResult.getResponse().getContentAsString();
		 
		 String expected="Student Deleted";
		 
		 assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expected);	
		
	}
	
	@Test
	void testDeleteStudent_notFound() throws Exception {
		Mockito.doThrow(StudentNotFoundException.class).when(service).deleteStudent(Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/student/{id}",s1.getStudentId())
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
		
	}
	
	@Test
	void updateStudentData() throws Exception{
		
		Student s2=new Student();
		s2.setStudentName("Rahul");
		s2.setClassTeacherId("1");
		s2.setEmergencyContactNumber("2341578374");
		s2.setGender("Female");
		s2.setStandard("V");
		s2.setStudentId(1);
		s2.setAddress(address);
		s2.setFacultyAllocated(faculty);
		s2.setSubject(subjectList);
		
		
		Mockito.when(service.updateStudent(s1,s1.getStudentId())).thenReturn(s2);
		
		 String content=objectWriter.writeValueAsString(s2);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.put("/student/{id}", s1.getStudentId())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
			
			mockMvc.perform(mockRequest)
			        .andExpect(status().isOk());
			
			ResponseEntity<String> msg=controller.updateStudent(s1,s1.getStudentId());
		    assertEquals(msg.getBody(),"Student data Updated");
				
	}
	
	@Test
	void updateStudentData_studentNotFOund() throws Exception{
		
		Student s2=new Student();
		s2.setStudentName("Rahul");
		s2.setClassTeacherId("1");
		s2.setEmergencyContactNumber("2341578374");
		s2.setGender("Female");
		s2.setStandard("V");
		s2.setStudentId(1);
		s2.setAddress(address);
		s2.setFacultyAllocated(faculty);
		s2.setSubject(subjectList);
		
		
		 Mockito.when(service.updateStudent(Mockito.any(Student.class),Mockito.anyInt())).thenThrow(StudentNotFoundException.class);
		
		 String content=objectWriter.writeValueAsString(s2);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.put("/student/{id}", 5)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content);
			
			mockMvc.perform(mockRequest)
			        .andExpect(status().isNotFound());
			        
		 
		 assertEquals(HttpStatus.NOT_FOUND,controller.updateStudent(s1,s1.getStudentId()).getStatusCode());
			
	}
	
	@DisplayName("Enroll new Student_val Name should not be null")
	@Test
	void testEnrollNewStudent_val() throws Exception {
		s1.setStudentName("");
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 MvcResult mvcResult=mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest())
		         .andReturn();
			
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
			 
	    String expected="{\"studentName\":\"must not be blank\"}";
			 
	    assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expected);
	}
	
	@DisplayName("Enroll new Student_val Invalid Number")
	@Test
	void testEnrollNewStudent_val_number() throws Exception {
		s1.setEmergencyContactNumber("45365");
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());	
	}
	
	@DisplayName("Enroll new Student_val Invalid Name")
	@Test
	void testEnrollNewStudent_val_name() throws Exception {
		s1.setStudentName("Sans@123");
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());
	     
		 
	}
	
	@DisplayName("Enroll new Student_val Invalid Name")
	@Test
	void testEnrollNewStudent_val_standard() throws Exception {
		s1.setStandard(null);		
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/enrollnewstudent")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());             
	}
		

}

package com.miniproject.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.serviceImpl.StudentServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;


import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest2 {
	
	private final static String BASE_URI = "http://localhost";
	
	@LocalServerPort
	private int port;

	@InjectMocks
	private StudentController controller;
	
	//@Mock
	@MockBean
	private StudentServiceImpl service;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter=objectMapper.writer();
	
	Student s1=new Student();
	Address address=new Address(1,"Pune","nagar","Sheri","India", "411014");
	Faculty faculty=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
	Subject subject=new Subject(1,"Maths",faculty,"V",45);
	List<Subject> subjectList=new ArrayList<>();
	
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
	
	@BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }
	
	@Test
	void testGetAllStudents() {
		//fail("Not yet implemented");
		List<Student> students=new ArrayList<>();
		students.add(s1);
		
		Mockito.when(service.getStudents()).thenReturn(students);
		
		given()
		.when()
		.get("/students")
		.then()
		.assertThat()
		.statusCode(200)
		.body("$.size()", Matchers.equalTo(1))
		.body("[0].studentName", Matchers.equalTo("Sanskruti"))
		.log()
		.all();
	}
	
	@Test
	void testGetAllStudents_ListEmpty() {
		List<Student> students=new ArrayList<>();
		Mockito.when(service.getStudents()).thenReturn(students);
		
		given()
		.when()
		.get("/students")
		.then()
		.assertThat()
		.statusCode(404);		
	}
	
	@Test
	void testEnrollNewStudent() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		String content=objectWriter.writeValueAsString(s1); 
		
		String response=given()
        .contentType(ContentType.JSON)
        .body(content)
        .log().all()
        .when()
        .post("/enrollnewstudent")
        .then()
        .assertThat().statusCode(200)
        .extract().body().asString();
		
		String expected="Student Details Saved";
		 
	    assertThat(response).isEqualToIgnoringWhitespace(expected);
	}
	
	@Test
	void testEnrollNewStudent_notAdded() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(null);
		
		String content=objectWriter.writeValueAsString(s1); 
		
		String response=given()
        .contentType(ContentType.JSON)
        .body(content)
        .log().all()
        .when()
        .post("/enrollnewstudent")
        .then()
        .assertThat().statusCode(200)
        .extract().body().asString();
		
		String expected="No Student was added";
		 
	    assertThat(response).isEqualToIgnoringWhitespace(expected);
	}
	
	@Test
	void testEnrollNewStudent_CapacityFull() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenThrow(ClassCapacityFullException.class);
		
		String content=objectWriter.writeValueAsString(s1); 
		
		given()
        .contentType(ContentType.JSON)
        .body(content)
        .log().all()
        .when()
        .post("/enrollnewstudent")
        .then()
        .assertThat().statusCode(400);
        
	}
	
	@Test
	void testEnrollNewStudent_SubjectNotAllocatedToStandardException() throws Exception {
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenThrow(SubjectNotAllocatedToStandardException.class);
		
		String content=objectWriter.writeValueAsString(s1); 
		
		given()
        .contentType(ContentType.JSON)
        .body(content)
        .log().all()
        .when()
        .post("/enrollnewstudent")
        .then()
        .assertThat().statusCode(400);
        
	}
	
	@Test
	void testDeleteStudent() throws Exception{
		Mockito.doNothing().when(service).deleteStudent(s1.getStudentId());
		String response=given()
		.contentType(ContentType.JSON)
		.pathParam("id", s1.getStudentId())
		.when()
		.delete("/student/{id}")
		.then()
		.assertThat()
		.statusCode(200)
		//.extract().body().asString();
		.extract().response().asString();
		
		String expected="Student Deleted";
		 
	    assertThat(response).isEqualToIgnoringWhitespace(expected);
	}
	
	@Test
	void testDeleteStudent_notFound() throws Exception{
		Mockito.doThrow(StudentNotFoundException.class).when(service).deleteStudent(Mockito.anyInt());
		given()
		.contentType(ContentType.JSON)
		.pathParam("id", s1.getStudentId())
		.when()
		.delete("/student/{id}")
		.then()
		.assertThat()
		.statusCode(404);
		
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
		
		String response=given()
        .contentType(ContentType.JSON)
        .pathParam("id", s1.getStudentId())
        .body(content)
        .log().all()
        .when()
        .put("/student/{id}")
        .then()
        .assertThat().statusCode(200)
        .extract().body().asString();
		
		String expected="Student data Updated";
		 
	    assertThat(response).isEqualToIgnoringWhitespace(expected);
	    
	    
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
		
		given()
        .contentType(ContentType.JSON)
        .pathParam("id", s1.getStudentId())
        .body(content)
        .log().all()
        .when()
        .put("/student/{id}")
        .then()
        .assertThat().statusCode(404);     
	}
	
	@DisplayName("Enroll new Student_val Name should not be null")
	@Test
	void testEnrollNewStudent_val() throws Exception {
		s1.setStudentName("");
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 String response=given()
			        .contentType(ContentType.JSON)
			        .body(content)
			        .log().all()
			        .when()
			        .post("/enrollnewstudent")
			        .then()
			        .assertThat().statusCode(400)
			        .extract().body().asString();
					
		 			String expected="{\"studentName\":\"must not be blank\"}";
					 
				    assertThat(response).isEqualToIgnoringWhitespace(expected);
				    
	}
	
	@DisplayName("Enroll new Student_val Invalid Number")
	@Test
	void testEnrollNewStudent_val_number() throws Exception {
		s1.setEmergencyContactNumber("4537");
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 String response=given()
			        .contentType(ContentType.JSON)
			        .body(content)
			        .log().all()
			        .when()
			        .post("/enrollnewstudent")
			        .then()
			        .assertThat().statusCode(400)
			        .extract().body().asString();
					
		 			String expected="{\"emergencyContactNumber\":\"Invalid number! PLease enter valid phone number\"}";
					 
				    assertThat(response).isEqualToIgnoringWhitespace(expected);				    
	}
	
	@DisplayName("Enroll new Student_val Invalid Name")
	@Test
	void testEnrollNewStudent_val_standard() throws Exception {
		s1.setStandard(null);		
		Mockito.when(service.enrollNewStudent(Mockito.any(Student.class))).thenReturn(s1);
		
		 String content=objectWriter.writeValueAsString(s1);
		 
		 String response=given()
			        .contentType(ContentType.JSON)
			        .body(content)
			        .log().all()
			        .when()
			        .post("/enrollnewstudent")
			        .then()
			        .assertThat().statusCode(400)
			        .extract().body().asString();
					
		 			String expected="{\"standard\":\"must not be blank\"}";
					 
				    assertThat(response).isEqualToIgnoringWhitespace(expected);	
		 
	}
		

}

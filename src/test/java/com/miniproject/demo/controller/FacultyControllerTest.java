package com.miniproject.demo.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.exception.FacultyNotFoundException;
import com.miniproject.demo.service.FacultyService;

@ExtendWith(SpringExtension.class)
class FacultyControllerTest {

	@Mock
	private FacultyService service;
	
	@InjectMocks
	private FacultyController controller;
	
    private MockMvc mockMvc;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter=objectMapper.writer();
	
	Address address=new Address(1,"Pune","nagar","Sheri","India", "411014");
	Faculty faculty=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
	
	@BeforeEach
	public void setUp() {
		this.mockMvc=MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@DisplayName("Enroll new faculty_Success")
	@Test
	void testAddFaculty() throws Exception {
		Mockito.when(service.addFaculty(Mockito.any(Faculty.class))).thenReturn(faculty);
		
		 String content=objectWriter.writeValueAsString(faculty);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/addfaculty")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 mockMvc.perform(mockRequest)
	             .andExpect(status().isCreated());
		 
		 ResponseEntity<String> msg=controller.addFaculty(faculty);
		 assertEquals(msg.getBody(),"Faculty Details Saved");		   	
	}
	
	@DisplayName("Enroll new faculty_not Added")
	@Test
	void testAddFaculty_notAdd() throws Exception {
		Mockito.when(service.addFaculty(Mockito.any(Faculty.class))).thenReturn(null);
		
		 String content=objectWriter.writeValueAsString(faculty);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.post("/addfaculty")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
		 
		 mockMvc.perform(mockRequest)
	             .andExpect(status().isBadRequest());
		 
		 ResponseEntity<String> msg=controller.addFaculty(faculty);
		 assertEquals(msg.getBody(),"No Faculty was added");		   	
	}
	
	@DisplayName("To get All Faculty")
	@Test
	void testGetAllFaculty() throws Exception {
		List<Faculty> f1=new ArrayList<>();
		f1.add(faculty);
		
		Mockito.when(service.getFaculty()).thenReturn(f1);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/faculty")
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.size()", is(f1.size())))
		        .andExpect(jsonPath("$[0].name", is("Radha")));
	}
	
	@DisplayName("To get All Faculty when list is Empty")
	@Test
	void testGetAllFaculty_ListEmpty() throws Exception {
		List<Faculty> f1=new ArrayList<>();
		Mockito.when(service.getFaculty()).thenReturn(f1);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/faculty")
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
	}
	
	@Test
	void testDeleteFaculty() throws Exception {
		Mockito.doNothing().when(service).deleteFaculty(faculty.getFacultyId());
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/faculty/{id}",faculty.getFacultyId())
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk());
		
		ResponseEntity<String> msg=controller.deleteFaculty(faculty.getFacultyId());
		assertEquals(msg.getBody(),"Faculty Deleted");
	}
	
	@Test
	void testDeleteFaculty_notFound() throws Exception {
		Mockito.doThrow(FacultyNotFoundException.class).when(service).deleteFaculty(Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/faculty/{id}",faculty.getFacultyId())
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
		
	}
	
	@Test
	void updateFaculty() throws Exception {
		Faculty f1=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
		
		Mockito.when(service.updateFaculty(faculty,faculty.getFacultyId())).thenReturn(f1);
		
		 String content=objectWriter.writeValueAsString(f1);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.put("/faculty/{id}", faculty.getFacultyId())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(content);
			
			mockMvc.perform(mockRequest)
			        .andExpect(status().isOk());
			
			ResponseEntity<String> msg=controller.updateFaculty(faculty,faculty.getFacultyId());
		    assertEquals(msg.getBody(),"Faculty Details Updated");
	}
	
	@Test
	void updateFaculty_facultyNotFound() throws Exception {
		Faculty f2=new Faculty(1,"Radha","Female",address,1,"VI","7594453729");
		
		Mockito.when(service.updateFaculty(Mockito.any(Faculty.class),Mockito.anyInt())).thenThrow(FacultyNotFoundException.class);
		
		 String content=objectWriter.writeValueAsString(f2);
		 
		 MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders
					.put("/faculty/{id}", faculty.getFacultyId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(content);
			
			mockMvc.perform(mockRequest)
			        .andExpect(status().isNotFound());
			
	}


}

package com.miniproject.demo.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miniproject.demo.dao.StudentRepository;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.service.AddressService;
import com.miniproject.demo.service.FacultyService;
import com.miniproject.demo.service.StudentService;
import com.miniproject.demo.service.SubjectService;


@Service
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentRepository repository;
		
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private FacultyService facultyService;
	
	@Autowired
	private SubjectService subjectService;
	

    static Map<String,Integer> map=new HashMap<>();
	static {
		map.put("I", 0);
		map.put("II", 0);
		map.put("III", 0);
		map.put("IV", 0);
		map.put("V", 0);
		map.put("VI", 0);
		map.put("VII", 0);
		map.put("VIII", 0);
		map.put("IX", 0);
		map.put("X", 0);	
	}
	
	static List<SubjectStandardMap> list=new ArrayList<>();
	

	//Adding new Student to database, returns the student added
	@Override
	public Student enrollNewStudent(Student student) throws ClassCapacityFullException, SubjectNotAllocatedToStandardException {
	
        
		if(checkStaticMap(student)){
		
			//int flag=subjectAllocatedToStandardCheck(student,list);
	

			if(subjectAllocatedToStandardCheck(student,list)==0) {
		
				    addressService.validateAddress(student);	
	    
				    subjectService.validateSubject(student);
					
	                facultyService.validateFaculty(student);
					
					Student s=repository.save(student);
					map.computeIfPresent(student.getStandard(), (k,v)->v+1);
					addSubjectStandardToList(student);
					return s;
					
			}else {
					throw new SubjectNotAllocatedToStandardException("No More subjects can be allocated to the standard in subject Entity and Teaching hours can't exceed 6hrs");
			}
	}else {
			
			throw new ClassCapacityFullException("The Standard you are trying to admit student is full");
	}
		
	}

	public boolean checkStaticMap(Student student) {
		boolean val=map.get(student.getStandard())<30;
		return val;
	}
	


	//returns the list of students from database
	@Override
	public List<Student> getStudents() {
		try {
		List<Student> list=repository.findAll();
		return list;
		}catch (Exception e) {
			throw e;
		}
	}

	//updating the details of student, returns the updated student
	@Override
	public Student updateStudent(Student student, int id) throws StudentNotFoundException {
	      Optional<Student> s=repository.findById(id);
	      if(s.isPresent()) {
	    	  Student s1=s.get();
	    	  s1.setAddress(student.getAddress());
		      s1.setClassTeacherId(student.getClassTeacherId());
		      s1.setEmergencyContactNumber(student.getEmergencyContactNumber());
		      s1.setFacultyAllocated(student.getFacultyAllocated());
		      s1.setGender(student.getGender());
		      s1.setStandard(student.getStandard());
		      s1.setStudentName(student.getStudentName());
		      s1.setSubject(student.getSubject());
		      return repository.save(s1);
	      }
	      else {
	    	  throw new StudentNotFoundException("Student is not present in Database");
	      }
	     	
	}

	//deleting student from database
	@Override
	public void deleteStudent(int id) throws StudentNotFoundException {
		Optional<Student> s1=repository.findById(id);
		if(s1.isPresent()) {
			repository.delete(s1.get());
		}
		else {
			throw new StudentNotFoundException("Student Does not exsist");
		}
		
	}
	
	
	
	class SubjectStandardMap{
		String name;
		String standard;
		public SubjectStandardMap(String name, String standard) {
			super();
			this.name = name;
			this.standard = standard;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStandard() {
			return standard;
		}
		public void setStandard(String standard) {
			this.standard = standard;
		}
		@Override
		public String toString() {
			return "SubjectStandardMap [name=" + name + ", standard=" + standard + "]";
		}	
		
	}
	
	//Creating list of subject allocated to standards
	public void addSubjectStandardToList(Student student) {
		
		for(Subject s1:student.getSubject()) {
			int flag=0;
			for(SubjectStandardMap sm:list) {
				if(s1.getsubjectName().equals(sm.getName()) && s1.getStandardAllocated().equals(sm.getStandard()))
				{
					flag=1;
				}
			}
			if(flag==0) {
			list.add(new SubjectStandardMap(s1.getsubjectName(), s1.getStandardAllocated()));
			}
		}
	}
	
	//Checking no of subjects allocated for given standard
	public int subjectAllocatedToStandardCheck(Student student,List<SubjectStandardMap> list) {
		for(Subject s1:student.getSubject()) {
			int count=0;
			for(SubjectStandardMap sm:list) {
				if(sm.getStandard().equals(s1.getStandardAllocated())) {
					count++;
				}
			}
			if(count>=6) {
				return 1;
			}
		}
		return 0;
	}
	
	
}

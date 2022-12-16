package com.miniproject.demo.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miniproject.demo.dao.AddressRepository;
import com.miniproject.demo.dao.FacultyRepository;
import com.miniproject.demo.dao.StudentRepository;
import com.miniproject.demo.dao.SubjectRepository;
import com.miniproject.demo.entity.Address;
import com.miniproject.demo.entity.Faculty;
import com.miniproject.demo.entity.Student;
import com.miniproject.demo.entity.Subject;
import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;
import com.miniproject.demo.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private FacultyRepository facultyRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private AddressRepository addressRepository;

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
	

	
	@Override
	public void enrollNewStudent(Student student) throws ClassCapacityFullException, SubjectNotAllocatedToStandardException {
	
        
		
		if(map.get(student.getStandard())<30){
		
			int flag=subjectAllocatedToStandardCheck(student,list);
	

			if(flag==0) {
		
				    validateAddress(student);
				
	                validateSubject(student);
					
					validateFaculty(student);
					
					repository.save(student);
					map.computeIfPresent(student.getStandard(), (k,v)->v+1);
					addSubjectStandardToList(student);
					
			}else {
					throw new SubjectNotAllocatedToStandardException("No More subjects can be allocated to the standard in subject Entity and Teaching hours can't exceed 6hrs");
			}
	}else {
			
			throw new ClassCapacityFullException("The Standard you are trying to admit student is full");
	}
		
	}

	@Override
	public List<Student> getStudents() {
	  
       System.out.println(subjectRepository.SubjectAllocatedToStandardCheck());
		
		List<Student> list=repository.findAll();
		return list;
	}

	@Override
	public void updateStudent(Student student, int id) throws StudentNotFoundException {
	      Optional<Student> s1=repository.findById(id);
	      if(s1.isPresent()) {
	    	  s1.get().setAddress(student.getAddress());
		      s1.get().setClassTeacherId(student.getClassTeacherId());
		      s1.get().setEmergencyContactNumber(student.getEmergencyContactNumber());
		      s1.get().setFacultyAllocated(student.getFacultyAllocated());
		      s1.get().setGender(student.getGender());
		      s1.get().setStandard(student.getStandard());
		      s1.get().setStudentName(student.getStudentName());
		      s1.get().setSubject(student.getSubject());
		      repository.save(s1.get());
	      }
	      else {
	    	  throw new StudentNotFoundException("Student is not present in Database");
	      }
	     	
	}

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
	
	public void validateAddress(Student student) {
		Address a=student.getAddress();
		Address address=addressRepository.findByCityAndAddressLine1AndAddressLine2AndPinCodeAndCountry(a.getCity(),a.getAddressLine1(),a.getAddressLine2(),a.getPinCode(),a.getCountry());
		if(address!=null) {
			student.setAddress(address);
		}	
	}
	
	public void validateFaculty(Student student) {
		Faculty faculty=facultyRepository.findByNameAndGenderAndContactNumber(student.getFacultyAllocated().getName(),student.getFacultyAllocated().getGender(),student.getFacultyAllocated().getContactNumber());
		if(faculty!=null)
		{
			student.setFacultyAllocated(faculty);			     
		}
	}
	
	public void validateSubject(Student student) {
		for(Subject s:student.getSubject()) {
			Faculty f1=facultyRepository.findByNameAndGenderAndContactNumber(s.getFacultyAllocated().getName(),s.getFacultyAllocated().getGender(),s.getFacultyAllocated().getContactNumber());
			if(f1!=null) {
				s.setFacultyAllocated(f1);
			}				
		}
		
	}
	
//	public int subjectAllocatedToStandardCheck(Student student) {
//		List li=subjectRepository.newSubjectAllocatedToStandardCheck();
//		for(Subject s1:student.getSubject()) {
//			  int count=0;
//			  for(int i=0;i<li.size();i++) {
//				  String s=li.get(i).toString();
//				  if(s.split(",")[1].equals(s1.getStandardAllocated())) {
//					  count++;
//				  }			  
//			  }
//			  if(count>=6) {
//				  return 1;
//			  }		  
//		   }
//		return 0;		
//	}
	
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
		//System.out.println(list);
		//return list;	
	}
	
	public int subjectAllocatedToStandardCheck(Student student,List<SubjectStandardMap> list) {
		for(Subject s1:student.getSubject()) {
			int count=0;
			for(SubjectStandardMap sm:list) {
				if(sm.getStandard().equals(s1.getStandardAllocated())) {
					count++;
				}
			}
			if(count>=6) {
				System.out.println(s1.getStandardAllocated());
				return 1;
			}
		}
		return 0;
	}
	
	

}

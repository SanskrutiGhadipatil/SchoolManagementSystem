package com.miniproject.demo.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.miniproject.demo.exception.ClassCapacityFullException;
import com.miniproject.demo.exception.FacultyNotFoundException;
import com.miniproject.demo.exception.StudentNotFoundException;
import com.miniproject.demo.exception.SubjectNotAllocatedToStandardException;

import jakarta.validation.ConstraintViolationException;



@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = StudentNotFoundException.class)
	 public ResponseEntity<Object> exception(StudentNotFoundException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);  
		//return exception.getMessage();
			
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = FacultyNotFoundException.class)
	 public ResponseEntity<Object> exception(FacultyNotFoundException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
		//return exception.getMessage();
			
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = ClassCapacityFullException.class)
	 public ResponseEntity<Object> exception(ClassCapacityFullException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		//return exception.getMessage();
			
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = SubjectNotAllocatedToStandardException.class)
	 public ResponseEntity<Object> exception(SubjectNotAllocatedToStandardException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		//return exception.getMessage();
			
	}
	
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map <String,String> handleInvalidArguments(MethodArgumentNotValidException ex){
		
		Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	    
	    }	
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = ConstraintViolationException.class)
	 public ResponseEntity<Object> exception(ConstraintViolationException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
				
	}
	

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	 public ResponseEntity<Object> exception(HttpMessageNotReadableException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
		
			
	}
	
	

}

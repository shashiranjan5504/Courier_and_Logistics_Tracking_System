package com.jsp.Courier_Logistics_Tracking_System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleIdNotFoundException(IdNotFoundException exp){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData(exp.getMessage());
		res.setMessage("Failure");
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(RecordNotAvailableException.class)
	public ResponseEntity<ResponseStructure<String>> handleRecordNotFoundException(RecordNotAvailableException exp){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData(exp.getMessage());
		res.setMessage("Failure");
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler(InvalidValidationException.class)
	public ResponseEntity<ResponseStructure<String>> handleCustomerValidationException(InvalidValidationException exp){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData(exp.getMessage());
		res.setMessage("Failure");
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
		
	}

	@ExceptionHandler(RecordDeletionNotAllowedException.class)
	public ResponseEntity<ResponseStructure<String>> handleRecordDeletionNotAllowedException(RecordDeletionNotAllowedException exp){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData(exp.getMessage());
		res.setMessage("Failure");
		res.setStatusCode(HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(res,HttpStatus.CONFLICT);
		
	}
	
	@ExceptionHandler(UpdationNotCompletedException.class)
	public ResponseEntity<ResponseStructure<String>> handleUpdationNotCompletedException(UpdationNotCompletedException exp){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData(exp.getMessage());
		res.setMessage("Updation Failed");
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
	}
	
	

}

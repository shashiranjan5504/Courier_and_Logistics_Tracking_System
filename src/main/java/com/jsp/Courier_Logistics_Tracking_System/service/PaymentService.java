package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Payment;
import com.jsp.Courier_Logistics_Tracking_System.entity.PaymentMethod;
import com.jsp.Courier_Logistics_Tracking_System.entity.PaymentStatus;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.exception.UpdationNotCompletedException;
import com.jsp.Courier_Logistics_Tracking_System.repository.PaymentRepository;

@RestController

public class PaymentService {
	
	@Autowired
	private PaymentRepository repo;

	public ResponseEntity<ResponseStructure<List<Payment>>> getAllPayment() {
		
		List<Payment> payments=repo.findAll();
		
		if(payments.isEmpty())
			throw new RecordNotAvailableException("Payment Record Not Available");
		
		ResponseStructure<List<Payment>> res=new ResponseStructure<>();
		res.setData(payments);
		res.setMessage("All Records of Payment Fetched Successfully.....");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Payment>> getPaymentDetailsById(Integer id) {
		if(id==null)
			throw new InvalidValidationException("Id must be passed...");
		
		
		Optional<Payment> opt=repo.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Payment Record not Available with the id "+id);
		Payment payment=opt.get();
		ResponseStructure<Payment> res=new ResponseStructure<>();
		res.setData(payment);
		res.setMessage("Payment Record Fetched Successfully....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByPaymentMethod(PaymentMethod paymentMethod) {
		
		if(paymentMethod==null)
			throw new InvalidValidationException("PaymentMethod data must be passed......");
		
		List<Payment> payments=repo.findByPaymentMethod(paymentMethod);
		if(payments.isEmpty())
			throw new RecordNotAvailableException("Payment Record not Available with paymentMethod "+paymentMethod);
		ResponseStructure<List<Payment>> res=new ResponseStructure<>();
		res.setData(payments);
		res.setMessage("Payment Records  Fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Payment>> updatePaymentDetails(Integer id, PaymentStatus paymentStatus) {
		if(id==null)
			throw new InvalidValidationException("Id must be passed.....");
		if(paymentStatus==null)
			throw new InvalidValidationException("paymentStatus Data must  be passed......");
		Optional<Payment> opt=repo.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Payment Record Not Available with the id "+id);
		Payment existedPayment=opt.get();
		
		PaymentStatus oldStatus=existedPayment.getPaymentStatus();
		PaymentStatus newStatus=paymentStatus;
		if(oldStatus==newStatus)
			throw new UpdationNotCompletedException("OldPaymentStatus  and  NewPaymentStatus must be different.....");
		
		//here we use switch statement to decide what value can be modified to what ,if we do not this constraint  then we  don't need this switch statement .but this is more genuine and logically correct  ;
		//switch(oldStatus) tells you which existing state you're coming from, and newStatus tells you whether the requested destination state is allowed.
		switch(oldStatus) {
		
		case PENDING:
			if(newStatus==PaymentStatus.FAILED)
				throw new UpdationNotCompletedException("PENDING can only be modified to COMPLETED & REFUNDED");
			break;
		case COMPLETED:
			if(newStatus==PaymentStatus.FAILED||newStatus==PaymentStatus.PENDING)
				throw new UpdationNotCompletedException("COMPLETED can only be modified to  REFUNDED");
			break;
		case FAILED:
			throw new UpdationNotCompletedException("FAILED cannot be modiffied");
			//here break is not required bcz throw will automatically come out of loop;
		case REFUNDED:
			throw new UpdationNotCompletedException("REFUNDED cannot be modified");
			
		
		}
		
		existedPayment.setPaymentStatus(newStatus);
		//storing the updated payment record in db  is important
		Payment updatedPayment=repo.save(existedPayment);
		
		ResponseStructure<Payment> res=new ResponseStructure<>();
		res.setData(updatedPayment);
		res.setMessage("Updation Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	

}

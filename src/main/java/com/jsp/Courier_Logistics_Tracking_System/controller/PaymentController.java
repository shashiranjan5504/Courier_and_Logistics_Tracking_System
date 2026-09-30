package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Payment;
import com.jsp.Courier_Logistics_Tracking_System.entity.PaymentMethod;
import com.jsp.Courier_Logistics_Tracking_System.entity.PaymentStatus;
import com.jsp.Courier_Logistics_Tracking_System.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private  PaymentService paymentService;
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Payment>>> getAllPayment(){
		return paymentService.getAllPayment();
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Payment>> getPaymentDetailsById(@PathVariable Integer id ){
		return paymentService.getPaymentDetailsById(id);
		
	}
	
	@PatchMapping("/{id}/{paymentStatus}")
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentDetails(@PathVariable Integer id, @PathVariable  PaymentStatus paymentStatus){
		return paymentService.updatePaymentDetails(id,paymentStatus);
	}
	
	
	@GetMapping("/paymentmethod/{paymentMethod}")
	public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentByPaymentMethod(@PathVariable PaymentMethod paymentMethod){
		return paymentService.getPaymentByPaymentMethod(paymentMethod);
	}



}

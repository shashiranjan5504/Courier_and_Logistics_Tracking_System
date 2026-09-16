package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Customer;
import com.jsp.Courier_Logistics_Tracking_System.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping
	public  ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer){
		return customerService.createCustomer(customer);
	}
	//2. doubt  hai
	
	
	
	@GetMapping("{id}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(@PathVariable Integer id ){
		return customerService.getCustomerById(id);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerByEmail(@PathVariable String email){
		return customerService.getCustomerByEmail(email);
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer(){
		return customerService.getAllCustomer();
	}
	
	@PutMapping
	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@RequestBody Customer customer){
		
		return customerService.updateCustomer(customer);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(@PathVariable Integer id ){
		return customerService.deleteCustomer(id);
		
	}
	
	@GetMapping("/contactNo/{contactNo}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerByContactNo(@PathVariable Long contactNo){
		return customerService.getCustomerByContactNo(contactNo);
	}
	@GetMapping("/{pageNo}/{pageSize}/{fieldName}")
	public ResponseEntity<ResponseStructure<Page<Customer>>> getCustomerByPaginationAndSorting(@PathVariable int pageNo,@PathVariable int pageSize,@PathVariable String fieldName){
		return customerService.getCustomerByPaginationAndSorting(pageNo,pageSize,fieldName);
	}
		
	
	

}

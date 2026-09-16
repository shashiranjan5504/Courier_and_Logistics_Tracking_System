package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Customer;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordDeletionNotAllowedException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.repository.CustomerRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.ShipmentRepository;

@Service
public class CustomerService {
	@Autowired
	private  ShipmentRepository shipmentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	public ResponseEntity<ResponseStructure<Customer>> createCustomer(Customer customer) {
		
		if(customer.getEmail()==null &&  customer.getEmail().isBlank())
			throw new InvalidValidationException("Email is required.........");
			
		
		if(customerRepository.existsByEmail(customer.getEmail()))
			throw new InvalidValidationException("Email must be unqiue.........");
		
		
		if(String.valueOf(customer.getPhoneNo()).length()!=10)
			throw new InvalidValidationException("Phone Number must  be 10 digit .........");
		
		
		if(customerRepository.existsByPhoneNo(customer.getPhoneNo()))
			throw new InvalidValidationException("Phone Number must  be unique..............");
		
		Customer savedCustomer=customerRepository.save(customer);
		ResponseStructure<Customer> res=new ResponseStructure<>();
		res.setData(savedCustomer);
		res.setMessage("Customer Record get created");
		res.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer() {
		List<Customer> customers=customerRepository.findAll();
		if(customers.isEmpty())
			throw new RecordNotAvailableException("No customer record found......."); 
		ResponseStructure<List<Customer>> res=new ResponseStructure<>();
		res.setData(customers);
		res.setMessage("All customer record got  displayed..........");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	

	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(Integer id) {
		Optional<Customer> opt=customerRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer record does not  exist with "+id+" in the db");
		Customer customer =opt.get();
		ResponseStructure<Customer> res=new ResponseStructure<>();
		res.setData(customer);
		res.setMessage("Customer record found....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	

	public ResponseEntity<ResponseStructure<Customer>> getCustomerByEmail(String email) {
		Optional<Customer> opt=customerRepository.findByEmail(email);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer record does not  exist with "+email+" in the db");
		Customer customer =opt.get();
		ResponseStructure<Customer> res=new ResponseStructure<>();
		res.setData(customer);
		res.setMessage("Customer record found....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
		
	}

	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(Customer customer) {
		if(customer.getId()==null)
			throw new InvalidValidationException("Id must be passed for updation");
		Optional<Customer> opt=customerRepository.findById(customer.getId());
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer record does not  exist with "+customer.getId()+" in the db");
		Customer existingCustomer=opt.get();
		
		if(customer.getEmail()==null || customer.getEmail().isBlank())
			throw new InvalidValidationException("Email must be passed...");
		
		//in updation logic validation logic for id =uniqueness is different
		if(!customer.getEmail().equals(existingCustomer.getEmail()) && customerRepository.existsByEmail(customer.getEmail()))
			throw new InvalidValidationException("Email must  be unique....");
		if(customer.getPhoneNo()==null)
			throw new InvalidValidationException("Phone numebr must be passed...");
		if(String.valueOf(customer.getPhoneNo()).length()!=10)
			throw new InvalidValidationException("Phone No must  be 10 digit.......");
		if(!customer.getPhoneNo().equals(existingCustomer.getPhoneNo()) && customerRepository.existsByPhoneNo(customer.getPhoneNo()))
			throw new InvalidValidationException("Phone No  must  be unique....");
			
		
		customerRepository.save(customer);
		ResponseStructure<Customer> res=new ResponseStructure<>();
		res.setData(customer);
		res.setMessage("Updated Successfully...");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
		
	}

	public ResponseEntity<ResponseStructure<String>> deleteCustomer(Integer id) {
		Optional<Customer> opt=customerRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer record does not  exist with "+id+" in the db");
		
		Customer customer=opt.get();
		//Not  More optimized
//		List<Shipment> shipments=customer.getShipments();
//		for(Shipment s:shipments ) {
//			if(s.getStatus()!=Status.DELIVERED && s.getStatus()!=Status.CANCELED)
//				throw new RecordDeletionNotAllowedException(" Shipment exist ...so cannot delete customer ");
//		}
		
		if(shipmentRepository.existsByCustomerIdAndStatusNotAndStatusNot(id,Status.DELIVERED,Status.CANCELLED))
			throw new RecordDeletionNotAllowedException("Customer Record cannot be deleted bcz of active shipments....");
		
		
		customerRepository.delete(customer);
		
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData("Customer got deleted...");
		res.setMessage("Successfully Deleted");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Customer>> getCustomerByContactNo(Long contactNo) {
		Optional<Customer> opt=customerRepository.findByContactNo(contactNo);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer record does not  exist with "+contactNo+" in the db");
		Customer customer =opt.get();
		ResponseStructure<Customer> res=new ResponseStructure<>();
		res.setData(customer);
		res.setMessage("Customer record found....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Page<Customer>>> getCustomerByPaginationAndSorting(int pageNo, int pageSize,
			String fieldName) {
		
		Page<Customer> customers=customerRepository.findAll(PageRequest.of(pageNo, pageSize,Sort.by(fieldName).ascending()));
		if(customers.isEmpty())
			throw new RecordNotAvailableException("Customer Record not found .......");
		ResponseStructure<Page<Customer>> res=new ResponseStructure<>();
		res.setData(customers);
		res.setMessage("Record found....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	

}

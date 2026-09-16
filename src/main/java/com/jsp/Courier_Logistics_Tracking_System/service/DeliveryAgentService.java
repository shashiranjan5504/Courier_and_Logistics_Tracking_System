package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.AvailableStatus;
import com.jsp.Courier_Logistics_Tracking_System.entity.DeliveryAgent;
import com.jsp.Courier_Logistics_Tracking_System.exception.IdNotFoundException;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordDeletionNotAllowedException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.repository.DeliveryAgentRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.ShipmentRepository;

@Service
public class DeliveryAgentService {
	
	@Autowired  
	private  ShipmentRepository   shipmentRepository;
	@Autowired
	private  DeliveryAgentRepository  agentRepository;

	public ResponseEntity<ResponseStructure<DeliveryAgent>> createDeliveryAgent(DeliveryAgent agent) {
		
		if(agent.getPhoneNo()==null)
			throw new InvalidValidationException("Contact Number must be passed...");
		if(String.valueOf(agent.getPhoneNo()).length()!=10)
			throw new InvalidValidationException("Contact Number must be 10 digits......");
		if(agentRepository.existsByPhoneNo(agent.getPhoneNo()))
			throw new InvalidValidationException("Contact Number must be unique ......");
			
		if(agent.getVehicleNo()==null)
			throw new InvalidValidationException("Vehicle Number must be passed...");
		
		if(agentRepository.existsByVehicleNo(agent.getVehicleNo()))
			throw new InvalidValidationException("Contact Number must be unique ......");
		
		ResponseStructure<DeliveryAgent> res=new ResponseStructure<>();
		
		DeliveryAgent savedAgent=agentRepository.save(agent);
		res.setData(savedAgent);
		res.setMessage("Record got saved successfully.......");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllDeliveryAgent() {
		
		List<DeliveryAgent> agents=agentRepository.findAll();
		if(agents.isEmpty())
			throw new RecordNotAvailableException("No record of Delivery Agent Exist in the DB");
		ResponseStructure<List<DeliveryAgent>> res =new ResponseStructure<>();
		
		res.setData(agents);
		res.setMessage("All Delivery Agent got Fetched");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentById(Integer id) {
		Optional<DeliveryAgent> opt= agentRepository.findById(id);
		
		if(opt.isEmpty())
			throw new IdNotFoundException("Delivery Agent record of this "+id+" is not exist in the db");
		DeliveryAgent existingAgent=opt.get();
		ResponseStructure<DeliveryAgent> res=new ResponseStructure<>();
		res.setData(existingAgent);
		res.setMessage("Delivery agent Found "  );
		res.setStatusCode(HttpStatus.OK.value());
	
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByVehicleNo(String vehicleNo) {
		Optional<DeliveryAgent> opt=agentRepository.findByVehicleNo(vehicleNo);
		if(opt.isEmpty())
			throw  new RecordNotAvailableException("Delivery  Agent Record with  this "+vehicleNo+" does not exist in the db");
		DeliveryAgent existingAgent=opt.get();
		ResponseStructure<DeliveryAgent> res=new ResponseStructure<>();
		res.setData(existingAgent);
		res.setMessage("Record Fetched successfully..");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new   ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByPhoneNo(Long phoneNo) {
		Optional<DeliveryAgent> opt=agentRepository.findByPhoneNo(phoneNo);
		if(opt.isEmpty())
			throw  new RecordNotAvailableException("Delivery  Agent Record with  this "+phoneNo+" does not exist in the db");
		DeliveryAgent existingAgent=opt.get();
		ResponseStructure<DeliveryAgent> res=new ResponseStructure<>();
		res.setData(existingAgent);
		res.setMessage("Record Fetched successfully..");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new   ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllDeliveryAgentRatingGreaterThan(Double rating) {
		
		List<DeliveryAgent> agents=agentRepository.findByRatingGreaterThan(rating);
		if(agents.isEmpty())
			throw new RecordNotAvailableException("No DeliveryAgent Record Found having greater rating than "+rating);
		ResponseStructure<List<DeliveryAgent>> res=new ResponseStructure<>();
		res.setData(agents);
		res.setMessage("Record fetcjed Successfully....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new  ResponseEntity<>(res,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<DeliveryAgent>> updateDeliveryAgent(DeliveryAgent agent){
		
		if(agent.getId()==null)
			throw new InvalidValidationException("Id must be passed for uodation....");
		
		
		Optional<DeliveryAgent> opt=agentRepository.findById(agent.getId());
		if(opt.isEmpty())
			throw new IdNotFoundException("DeliveryAgent with this "+agent.getId()+" does not exist  in the db");
		
		DeliveryAgent existingAgent=opt.get();
		
		if(agent.getPhoneNo()==null)
			throw new InvalidValidationException("Contact Number must be passed...");
		
		if(String.valueOf(agent.getPhoneNo()).length()!=10)
			throw new InvalidValidationException("Contact Number must be 10 digits......");
		
		if(!agent.getPhoneNo().equals(existingAgent.getPhoneNo())&&agentRepository.existsByPhoneNo(agent.getPhoneNo()))
			throw new InvalidValidationException("Contact Number must be unique ......");
			
		if(agent.getVehicleNo()==null)
			throw new InvalidValidationException("Vehicle Number must be passed...");
		
		if(!agent.getVehicleNo().equals(existingAgent.getVehicleNo())&&agentRepository.existsByVehicleNo(agent.getVehicleNo()))
			throw new InvalidValidationException("Vehicle Number must be unique ......");
		
		agentRepository.save(agent);//here  the agent send by user will be saved not  existingAgent
		ResponseStructure<DeliveryAgent> res=new ResponseStructure<>();
		res.setData(agent);
		res.setMessage("Record  updated Successfully...");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new 	ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteDeliveryAgent(Integer id) {
		
		Optional<DeliveryAgent> opt = agentRepository.findById(id);
		if(opt.isEmpty())
			throw new IdNotFoundException("DeliveryAgent with "+id+" dooes not exist in the db");
		
		if(shipmentRepository.existsByDeliveryAgentId(id))
			throw new RecordDeletionNotAllowedException("DeliveryAgent Record  cannot  be deleted as tehre is shipment assigned to it ...");
		
		DeliveryAgent existingAgent=opt.get();
		agentRepository.delete(existingAgent);
		
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData("Deleted Successfully......");
		res.setMessage("Success");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<DeliveryAgent>> updateDeliveryAgentAvailability(Integer id,
			AvailableStatus status) {
		
		 // 1. Validate ID
	    if (id == null)
	        throw new InvalidValidationException("DeliveryAgent id must be passed...");

	    // 2. Validate status
	    if (status == null)
	        throw new InvalidValidationException("Availability status must be passed...");
	    
	    
		Optional<DeliveryAgent> opt=agentRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("DeliveryAgent  Record with "+id+" does not exist  in the db");
		DeliveryAgent existingAgent=opt.get();
		
		 existingAgent.setAvailableStatus(status);
		
		 //this  is much cleaner  and easy  to read
		 DeliveryAgent updatedAgent =agentRepository.save(existingAgent);
		 
		 
		ResponseStructure<DeliveryAgent>  res=new ResponseStructure<>();
		res.setData(updatedAgent);
		res.setMessage("Availability updated..");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	

}

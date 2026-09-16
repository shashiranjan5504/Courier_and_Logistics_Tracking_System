package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.AvailableStatus;
import com.jsp.Courier_Logistics_Tracking_System.entity.DeliveryAgent;
import com.jsp.Courier_Logistics_Tracking_System.service.DeliveryAgentService;

@RestController
@RequestMapping("/deliveryagent")
public class DeliveryAgentController {
	@Autowired
	private DeliveryAgentService agentService; 
	
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> createDeliveryAgent(@RequestBody DeliveryAgent agent){
		return agentService.createDeliveryAgent(agent);
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllDeliveryAgent(){
		return agentService.getAllDeliveryAgent();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentById(@PathVariable Integer id){
		return agentService.getDeliveryAgentById(id);
	}
	@GetMapping("/vehicleNO/{vehicleNo}")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByVehicleNo(@PathVariable String vehicleNo){
		return agentService.getDeliveryAgentByVehicleNo(vehicleNo);
	}
	@GetMapping("/contactNo/{phoneNo}")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByPhoneNo(@PathVariable Long phoneNo){
		return agentService.getDeliveryAgentByPhoneNo(phoneNo);
	}
	///check  this cause issue or not 
	@GetMapping("/{rating}")
	public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllDeliveryAgentRatingGreaterThan(@PathVariable Double rating){
		return agentService.getAllDeliveryAgentRatingGreaterThan(rating);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> updateDeliveryAgent(@RequestBody DeliveryAgent agent){
		return agentService.updateDeliveryAgent(agent);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteDeliveryAgent(@PathVariable Integer id){
		return agentService.deleteDeliveryAgent(id);
	}
	@PatchMapping("/update/{id}/{availabilityStatus}")
	public ResponseEntity<ResponseStructure<DeliveryAgent>> updateDeliveryAgentAvailability(@PathVariable Integer id,@PathVariable  AvailableStatus status ){
		return agentService.updateDeliveryAgentAvailability(id,status);
	}
	
 	
	
	

}

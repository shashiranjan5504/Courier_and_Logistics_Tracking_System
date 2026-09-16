package com.jsp.Courier_Logistics_Tracking_System.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.AvailableStatus;
import com.jsp.Courier_Logistics_Tracking_System.entity.DeliveryAgent;
import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.repository.CustomerRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.DeliveryAgentRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.ShipmentRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.WarehouseRepository;

@Service
public class ShipmentService {
	
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private  WarehouseRepository warehouseRepository;
	
	@Autowired
	public  DeliveryAgentRepository deliveryAgentRepository;
	
	public ResponseEntity<ResponseStructure<Shipment>> createShipment(Shipment shipment) {
		
		
		//tracking number is mandatory 
		if(shipment.getTrackingNo()==null || shipment.getTrackingNo().isBlank())
			throw new InvalidValidationException("Tracking Number must be provided...");
		//tracking number unique
		if(shipmentRepository.existsByTrackingNo(shipment.getTrackingNo()))
			throw new InvalidValidationException("Tracking Number must  be unqiue.....");
		
		//customer info must be passed
		if(shipment.getCustomer()==null)
			throw new InvalidValidationException("Customer data must be passed....");
		//customer id must be passed
		if(shipment.getCustomer().getId()==null)
			throw new InvalidValidationException("Customer Id must be passed....");
		//customer info not found
		if(!customerRepository.existsById(shipment.getCustomer().getId()))
			throw new InvalidValidationException("Shipment data cannot be saved as it has no customer in the db");
		//warehouse data must be passed
		if(shipment.getWarehouse()==null)
			throw new InvalidValidationException("Warehouse data must be provided.....");
		//warehouse id must be passed 
		if(shipment.getWarehouse().getId()==null)
			throw new InvalidValidationException("Warehouse Id must be passed....");
		//warehouse info not  found
		if(!warehouseRepository.existsById(shipment.getWarehouse().getId()))
			throw new InvalidValidationException("Shipment data cannot be saved as it has no warehouse in the db");
		//delivery agent data must be passed
		if(shipment.getDeliveryAgent()==null)
			throw new InvalidValidationException("Delivery agent must be passed....");
		//deliveryAgent id must be passed
		if(shipment.getDeliveryAgent().getId()==null)
			throw new InvalidValidationException("DeliveryAgent Id must be passed....");
		//delivery agent  is not there 
		if(!deliveryAgentRepository.existsById(shipment.getDeliveryAgent().getId()))
			throw new InvalidValidationException("Shipment data cannot be saved as it has no deliveryagent in the db");
		//PackageEntity data is mandatory
		if(shipment.getPackageEntity()==null)
			throw new InvalidValidationException("packageEntity data must be passed....");
		//Payment  data  is mandatory
		if(shipment.getPayment()==null)
			throw new InvalidValidationException("Payment data must be passed.....");
		//Tracking history  is mandatory
		if(shipment.getTrackingHistories()==null)
			throw new InvalidValidationException("Tracking histories data  must be passed.....");
		
		//validating amount before operation on amount which may causes null pointer exception
		if(shipment.getPayment().getAmount()==null)
			throw new InvalidValidationException("Amount muust  be passed....");
		if(shipment.getPayment().getAmount()<=0)
			throw new InvalidValidationException("Amount must be positive........");
		
		// extra fragile cost addition
		if(shipment.getPackageEntity().isFragile()) {
			Double fixedCharge=150.00;
			Double	updatedAmount=shipment.getPayment().getAmount()+fixedCharge;
			
			shipment.getPayment().setAmount(updatedAmount);
			
		}
		
		//checking the weight of shipment  ,weight must be positive
		if (shipment.getWeight() == null || shipment.getWeight() <= 0)
		    throw new InvalidValidationException("Weight must be greater than 0...");
			
		// weight based cost 
		if(shipment.getWeight()>20.00) {
			
			
			Double extraWeight=shipment.getWeight()-20.00;
			Double extraAmount=extraWeight*50.00;// rs 50 per kg extra 
			
			Double updatedAmount=shipment.getPayment().getAmount()+extraAmount;
			
			shipment.getPayment().setAmount(updatedAmount);
			
			
		}
		
		
		DeliveryAgent assignedAgent=deliveryAgentRepository.findById(shipment.getDeliveryAgent().getId()).orElseThrow();
		if(assignedAgent.getAvailableStatus()!=AvailableStatus.Available) {
			throw new  InvalidValidationException("Delivery Agent is not Available");
		}
		shipment.setDeliveryAgent(assignedAgent);
		assignedAgent.setAvailableStatus(AvailableStatus.NOT_AVAILABLE);
		deliveryAgentRepository.save(assignedAgent);
		
		

			 
		 
						
				
		shipmentRepository.save(shipment);
		
		ResponseStructure<Shipment> res=new ResponseStructure<>();
		
		res.setData(shipment);
		res.setMessage("Shipment Record got saved...");
		res.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<>(res,HttpStatus.CREATED);
		
	}
	
	
	
	
	

}

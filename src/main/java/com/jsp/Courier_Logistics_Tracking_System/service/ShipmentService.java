package com.jsp.Courier_Logistics_Tracking_System.service;



import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.jsp.Courier_Logistics_Tracking_System.entity.AvailableStatus;
import com.jsp.Courier_Logistics_Tracking_System.entity.Customer;
import com.jsp.Courier_Logistics_Tracking_System.entity.DeliveryAgent;
import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.entity.TrackingHistory;
import com.jsp.Courier_Logistics_Tracking_System.entity.Warehouse;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.exception.UpdationNotCompletedException;
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

		//Tracking history is not there
		if (shipment.getTrackingHistories().isEmpty())
			throw new InvalidValidationException("Tracking histories must not be empty");

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

		// Customer validation
		Customer dbCustomer = customerRepository.findById(shipment.getCustomer().getId())
				.orElseThrow(() -> new RecordNotAvailableException("No customer found"));

		// Warehouse validation
		Warehouse dbWarehouse = warehouseRepository.findById(shipment.getWarehouse().getId())
				.orElseThrow(() -> new RecordNotAvailableException("No warehouse found"));

		// Delivery agent validation
		DeliveryAgent dbAgent = deliveryAgentRepository.findById(shipment.getDeliveryAgent().getId())
				.orElseThrow(() -> new RecordNotAvailableException("No delivery agent found"));

		// Relationship mapping
		shipment.setCustomer(dbCustomer);
		shipment.setWarehouse(dbWarehouse);
		shipment.setDeliveryAgent(dbAgent);

		shipment.getPayment().setShipment(shipment);
		shipment.getPackageEntity().setShipment(shipment);
		for (TrackingHistory history : shipment.getTrackingHistories()) {
			history.setShipment(shipment);
		}

		// Set timestamps
		shipment.setShipmentDateTime(LocalDateTime.now());
		shipment.getPayment().setPaymentDateTime(LocalDateTime.now());

		shipmentRepository.save(shipment);
		
		ResponseStructure<Shipment> res=new ResponseStructure<>();
		
		res.setData(shipment);
		res.setMessage("Shipment Record got saved...");
		res.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<>(res,HttpStatus.CREATED);
		
	}


	public ResponseEntity<ResponseStructure<List<Shipment>>> getAllShipments() {

		List<Shipment> shipments=shipmentRepository.findAll();
		if(shipments.isEmpty())
			throw new RecordNotAvailableException("No Shipment Record Found");
		ResponseStructure<List<Shipment>> res=new ResponseStructure<>();
		res.setData(shipments);
		res.setMessage("Shipments Record got Fetched");
		res.setStatusCode(HttpStatus.OK.value());


		return new ResponseEntity<>(res,HttpStatus.OK);


	}


	public ResponseEntity<ResponseStructure<Shipment>> getShipmentDetailsById(Integer id) {
		if(id==null)
			throw new InvalidValidationException("Id must be passed...");

		Optional<Shipment> opt=shipmentRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Shipment Record Not Found with id "+id);
		Shipment s=opt.get();
		ResponseStructure<Shipment> res=new ResponseStructure<>();
		res.setData(s);
		res.setMessage("Shipment Record got Fetched");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Shipment>> getShipmentDetailsByTrackingNumber(String trackingNumber) {
		if(trackingNumber==null)
			throw new InvalidValidationException("trackingNumber must be passed...");

		Optional<Shipment> opt=shipmentRepository.findByTrackingNumber(trackingNumber);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Shipment Record Not Found with trackingNumber "+trackingNumber);
		Shipment s=opt.get();
		ResponseStructure<Shipment> res=new ResponseStructure<>();
		res.setData(s);
		res.setMessage("Shipment Record got Fetched");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Shipment>> updateStatus(Integer id, Status status) {
		Optional<Shipment> dbShipment = shipmentRepository.findById(id);
		if (dbShipment.isEmpty())
			throw new RecordNotAvailableException("Shipment with Id: " + id + " does not exist");

		if (dbShipment.get().getStatus() == status)
			throw new InvalidValidationException("Shipment is already in " + status + " status");

		switch (dbShipment.get().getStatus()) {
			case BOOKED:
				if (status != Status.PICKED_UP && status != Status.CANCELLED)
					throw new InvalidValidationException("BOOKED shipment can only be changed to PICKED_UP or CANCELLED");
				break;
			case PICKED_UP:
				if (status != Status.IN_TRANSIT && status != Status.CANCELLED)
					throw new InvalidValidationException("PICKED_UP shipment can only be changed to IN_TRANSIT or CANCELLED");
				break;
			case IN_TRANSIT:
				if (status != Status.OUT_FOR_DELIVERY && status != Status.CANCELLED)
					throw new InvalidValidationException("IN_TRANSIT shipment can only be changed to OUT_FOR_DELIVERY or CANCELLED");
				break;
			case OUT_FOR_DELIVERY:
				if (status != Status.DELIVERED)
					throw new InvalidValidationException("OUT_FOR_DELIVERY shipment can only be changed to DELIVERED");
				break;
			case DELIVERED:
				throw new InvalidValidationException("DELIVERED shipment can't be modified");
			case CANCELLED:
				throw new InvalidValidationException("CANCELLED shipment can't be modified");
		}

		Shipment shipment = dbShipment.get();
		shipment.setStatus(status);

		if (status == Status.DELIVERED) {
			shipment.setDeliveryDate(LocalDate.now());
		}

		Shipment updatedShipment = shipmentRepository.save(shipment);

		ResponseStructure<Shipment> res = new ResponseStructure<>();
		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Shipment status updated successfully");
		res.setData(updatedShipment);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Shipment>> assignWarehouseToShipment(Integer shipmentId, Integer warehouseId) {
		if(shipmentId==null)
			throw new InvalidValidationException("shipmentId must be passed");
		if(warehouseId==null){
			throw new InvalidValidationException("warehouseId must be passed");
		}
		Optional<Shipment> opt=shipmentRepository.findById(shipmentId);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Shipment record not available");

		Shipment dbShipment=opt.get();
		Optional<Warehouse> opt1=warehouseRepository.findById(warehouseId);
		if(opt1.isEmpty())
			throw new RecordNotAvailableException("Warehosue record not found");
		Warehouse assignWarehouse=opt1.get();
		dbShipment.setWarehouse(assignWarehouse);

		Shipment updatedShipment=shipmentRepository.save(dbShipment);

		ResponseStructure<Shipment>res =new ResponseStructure<>();
		res.setData(updatedShipment);
		res.setMessage("Warehouse Assigned successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return  new ResponseEntity<>(res,HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<Shipment>> assignDeliveryAgentToShipment(Integer shipmentId, Integer deliveryAgentId) {

		if(shipmentId==null)
			throw new InvalidValidationException("shipmentId must be passed");
		if(deliveryAgentId==null){
			throw new InvalidValidationException("deliveryAgentId must be passed");
		}
		Optional<Shipment> opt=shipmentRepository.findById(shipmentId);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Shipment record not available");

		Shipment dbShipment=opt.get();

		Optional<DeliveryAgent> opt1=deliveryAgentRepository.findById(deliveryAgentId);
		if(opt1.isEmpty())
			throw new RecordNotAvailableException("DeliveryAgent record not found");

		DeliveryAgent assignDeliveryAgent=opt1.get();

		if(assignDeliveryAgent.getAvailableStatus()!=AvailableStatus.Available)
			throw new UpdationNotCompletedException("Delivery Agent is not avaialble ");

		dbShipment.setDeliveryAgent(assignDeliveryAgent);

		Shipment updatedShipment=shipmentRepository.save(dbShipment);
		ResponseStructure<Shipment> res= new ResponseStructure<>();
		res.setData(updatedShipment);
		res.setMessage("DeliveryAgent Assigned Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);


	}

	public ResponseEntity<ResponseStructure<String>> deleteShipmentById(Integer id) {
		if(id==null)
			throw new InvalidValidationException("Id must be passed...");

		if(!shipmentRepository.existsById(id))
			throw new RecordNotAvailableException("Record Not  Available ,Deletion Not possible");
		shipmentRepository.deleteById(id);
		ResponseStructure<String> res= new ResponseStructure<>();
		res.setData("Shipment Data got deleted");
		res.setMessage("Successfully Deleted ");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByCustomerId(Integer id) {
		if(id==null)
			throw new InvalidValidationException("Id must be Passed...");
		Optional<Customer> opt=customerRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Customer  Id is not  valid .....");
		Customer customer=opt.get();
		List<Shipment> shipments=customer.getShipments();
		if(shipments.isEmpty())
			throw new RecordNotAvailableException("Customer with id "+id+"has not any shipment  record");
		ResponseStructure<List<Shipment>> res=new ResponseStructure<>();
		res.setData(shipments);
		res.setMessage("Shipment  Record  Fetched");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByDeliveryAgentId(Integer id) {
		if(id==null)
			throw new InvalidValidationException("Id must be Passed");
		Optional<DeliveryAgent> opt=deliveryAgentRepository.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException("Delivery Agent id is not valid");
		DeliveryAgent deliveryAgent=opt.get();
		List<Shipment> shipments=deliveryAgent.getShipments();
		if(shipments.isEmpty())
			throw new RecordNotAvailableException("Shipments Record Not Available");
		ResponseStructure<List<Shipment>> res=new ResponseStructure<>();
		res.setData(shipments);
		res.setMessage("Shipment  Record Fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsBySourceAndDestination(String source, String destination) {
		if(source==null || source.isBlank())
			throw new InvalidValidationException("source must be passed ");
		if(destination==null||destination.isBlank())
			throw new InvalidValidationException("destination must be  passed");
		List<Shipment> shipments=shipmentRepository.findBySourceAndDestination(source,destination);
		if(shipments.isEmpty())
			throw new RecordNotAvailableException("Shipments Record Not Available");
		ResponseStructure<List<Shipment>> res=new ResponseStructure<>();
		res.setData(shipments);
		res.setMessage("Shipments Record Fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByDeliveryDate(LocalDate deliveryDate) {
		if(deliveryDate==null)
			throw new InvalidValidationException("deliveryDate must be passed");
		List<Shipment> shipments=shipmentRepository.findByDeliveryDate(deliveryDate);
		if(shipments.isEmpty())
			throw new RecordNotAvailableException("Shipment  Record  not  Available");
		ResponseStructure<List<Shipment>> res=new ResponseStructure<>();
		res.setData(shipments);
		res.setMessage("Shipments  Record Fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<Page<Shipment>>> getShipmentDetailsByPaginationAndSorting(Integer pageNumber,Integer pageSize, String fieldName) {

		if(pageNumber==null)
			throw new InvalidValidationException("pageNumber must be passed");
		if(pageSize==null)
			throw new InvalidValidationException("PageSize must be passed");
		if(fieldName==null||fieldName.isBlank())
			throw new InvalidValidationException("Field name must be passed");

		Page<Shipment> pages=shipmentRepository.findAll(PageRequest.of(pageNumber,pageSize, Sort.by(fieldName)));
		if(pages.isEmpty())
			throw new RecordNotAvailableException("No Shipment Record Avaialble");

		ResponseStructure<Page<Shipment>> res=new ResponseStructure<>();
		res.setData(pages);
		res.setMessage("Shipment Record Fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(res,HttpStatus.OK);




        }


}

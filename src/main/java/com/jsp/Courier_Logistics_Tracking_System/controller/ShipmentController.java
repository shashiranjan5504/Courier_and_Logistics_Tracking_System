package com.jsp.Courier_Logistics_Tracking_System.controller;

import com.jsp.Courier_Logistics_Tracking_System.entity.Customer;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.service.ShipmentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
	
	@Autowired
	private ShipmentService serv;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Shipment>> createShipment(@RequestBody Shipment shipment){
		return serv.createShipment(shipment);
		
	}
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Shipment>>> getAllShipments(){
		return serv.getAllShipments();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Shipment>> getShipmentById(@PathVariable Integer id){
		return serv.getShipmentDetailsById(id);
	}

	@GetMapping("/trackingNumber,/{trackingNumber}")
	public ResponseEntity<ResponseStructure<Shipment>> getShipmentDetailsByTrackingNumber(@PathVariable  String trackingNumber){
		return serv.getShipmentDetailsByTrackingNumber(trackingNumber);
	}
	@PatchMapping("update/{id}/{status}")
	public ResponseEntity<ResponseStructure<Shipment>> updateStatus(@PathVariable Integer id, @PathVariable Status status) {
		return serv.updateStatus(id,status);
	}
	@PatchMapping("{shipmentId/assignDeliveryAgent/{deliveryAgentId}}")
	public ResponseEntity<ResponseStructure<Shipment>> assignDeliveryAgent(@PathVariable Integer shipmentId,@PathVariable Integer deliveryAgentId){
		return serv.assignDeliveryAgentToShipment(shipmentId,deliveryAgentId);
	}

	@PatchMapping("{shipmentId}/assignwarehouse/{warehouseId}")
	public ResponseEntity<ResponseStructure<Shipment>> assignWarehouse(@PathVariable Integer shipmentId,@PathVariable Integer warehouseId){
		return serv.assignWarehouseToShipment(shipmentId,warehouseId);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteShipmentById(@PathVariable Integer id){
		return serv.deleteShipmentById(id);
	}


	@GetMapping("/bycustomer/{id}")
	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByCustomer(@PathVariable Integer id){
		return serv.getShipmentDetailsByCustomerId(id);

	}

	@GetMapping("/bydeliveryagent/{id}")
	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByDeliveryAgentId(@PathVariable Integer id){
		return serv.getShipmentDetailsByDeliveryAgentId(id);
	}

	@GetMapping("/{source}/{destination}")
	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsBySourceAndDestination(@PathVariable String source,@PathVariable String destination){
		return serv.getShipmentDetailsBySourceAndDestination(source,destination);
	}
	@GetMapping("/bydeliverydate/{deliveryDate}")
	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentDetailsByDeliveryDate(@PathVariable LocalDate deliveryDate){
		return serv.getShipmentDetailsByDeliveryDate(deliveryDate);
	}

	@GetMapping("/bypagination&sorting/{pageNumber}/{pageSize}/{fieldName}")
	public ResponseEntity<ResponseStructure<Page<Shipment>>> getShipmentDetailsByPaginationAndSorting(@PathVariable Integer pageNumber,@PathVariable Integer pageSize,@PathVariable String fieldName){
		return serv.getShipmentDetailsByPaginationAndSorting(pageNumber,pageSize,fieldName);
	}





}

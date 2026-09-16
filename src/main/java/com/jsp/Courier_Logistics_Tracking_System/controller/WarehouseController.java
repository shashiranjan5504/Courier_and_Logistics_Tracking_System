package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Warehouse;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.repository.WarehouseRepository;
import com.jsp.Courier_Logistics_Tracking_System.service.WarehouseService;

@RestController
public class WarehouseController {
	
	@Autowired
	private WarehouseService warehouseService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Warehouse>> createWarehouse(@RequestBody Warehouse warehouse){
		return  warehouseService.createWarehouse(warehouse);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Warehouse>>> getAllWarehouses(){
		return warehouseService.getAllWarehouses();
	}
	
	@GetMapping("{/id}")
	public ResponseEntity<ResponseStructure<Warehouse>> getCustomerById(@PathVariable Integer id){
		return warehouseService.getWarehouseById(id);
		
	}
	
	@GetMapping("/location/{location}")
	public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehouseByLocation(@PathVariable String location){
		return warehouseService.getWarehouseByLocation(location);
	}
	
	@GetMapping("/capacity/{capacity}")
	public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehousesByCapacityGreaterThan(@PathVariable Double capacity){
		return warehouseService.getWarehousesByCapacityGreaterThan(capacity);
	}
	
	@GetMapping("/update")
	public ResponseEntity<ResponseStructure<Warehouse>> updateWarehouse(@RequestBody Warehouse warehouse){
		return  warehouseService.updateWarehouse(warehouse);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> deleteWarehouseById(@RequestBody Warehouse warehouse){
		return warehouseService.deleteWarehouse(warehouse);
	}
	
	
	

}

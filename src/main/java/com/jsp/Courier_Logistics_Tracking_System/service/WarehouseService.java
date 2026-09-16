package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;

import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.entity.Warehouse;
import com.jsp.Courier_Logistics_Tracking_System.exception.IdNotFoundException;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordDeletionNotAllowedException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.repository.ShipmentRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.WarehouseRepository;

@Service
public class WarehouseService {
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private WarehouseRepository warehouseRepository;

	public ResponseEntity<ResponseStructure<Warehouse>> createWarehouse(Warehouse warehouse) {
		if(warehouse.getContactNo()==null)
			throw new InvalidValidationException("Contact Number must be passed");
		
		if(String.valueOf(warehouse.getContactNo()).length()!=10)
			throw new InvalidValidationException("Contact No must be of 10 digit.......");
		
		if(warehouseRepository.existsByContactNo(warehouse.getContactNo()))
			throw new InvalidValidationException("Contact No must be unique........");
		
		Warehouse savedWarehouse=warehouseRepository.save(warehouse);
		ResponseStructure<Warehouse> res=new ResponseStructure<>();
		res.setData(savedWarehouse);
		res.setMessage("Warehouse Record got Created.......");
		res.setStatusCode(HttpStatus.CREATED.value());
		
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Warehouse>>> getAllWarehouses() {
		List<Warehouse> warehouses=warehouseRepository.findAll();
		if(warehouses.isEmpty())
			throw new RecordNotAvailableException("Warehouse Reccord not Available.....");
		ResponseStructure<List<Warehouse>> res=new ResponseStructure<>();
		res.setData(warehouses);
		res.setMessage("Warehouse Record Found....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Warehouse>> getWarehouseById(Integer id) {
		Optional<Warehouse> opt=warehouseRepository.findById(id);
		if(opt.isEmpty())
			throw new IdNotFoundException("Warehouse Record not  exist with "+id+" in the DB");
		Warehouse warehouse=opt.get();
		ResponseStructure<Warehouse> res=new ResponseStructure<>();
		res.setData(warehouse);
		res.setMessage("");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}

	public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehouseByLocation(String location) {
		
		List<Warehouse> warehouses=warehouseRepository.findByLocation(location);
		
		if(warehouses.isEmpty())
			throw new RecordNotAvailableException("No Warehouse record  found ... ");
		
		ResponseStructure<List<Warehouse>> res=new ResponseStructure<>();
		res.setData(warehouses);
		res.setMessage("Warehouse Record found");
		res.setStatusCode(HttpStatus.ACCEPTED.value());
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehousesByCapacityGreaterThan(Double capacity) {
		
		List<Warehouse> warehouses=warehouseRepository.findByCapacityGreaterThan(capacity);
		if(warehouses.isEmpty())
			throw new RecordNotAvailableException("No Warehouse  record found....");
		
		ResponseStructure<List<Warehouse>> res=new ResponseStructure<>();
		res.setData(warehouses);
		res.setMessage("Warehouse Record found.... ");
		res.setStatusCode(HttpStatus.ACCEPTED.value());
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}

	

	public ResponseEntity<ResponseStructure<String>> deleteWarehouse(Warehouse warehouse) {
		
		Optional<Warehouse> opt=warehouseRepository.findById(warehouse.getId());
		if(opt.isEmpty())
			throw new IdNotFoundException("No Warehouse Record Not found...");
		
		Warehouse existingWarehouse=opt.get();
		
	
		//this will load all the shipment whether it is active or not 
//		for(Shipment sh : existingWarehouse.getShipments()) {
//			if(!sh.getStatus().equals(Status.DELIVERED)&& !sh.getStatus().equals(Status.CANCELED))
					// and also for comapring enum  sh.getStatus!=Status.DELIVERED this one is recommended 
//				throw new RecordDeletionNotAllowedException("Warehouse Record cannot be deleted bcz of active shipments....");
		
		//More  Efficient (doesn't load all shipments)
		if(shipmentRepository.existsByWarehouseIdAndStatusNotAndStatusNot(existingWarehouse.getId(),Status.CANCELLED,Status.DELIVERED)) {
			throw new RecordDeletionNotAllowedException("Warehouse Record cannot be deleted bcz of active shipments....");
				
		}
		warehouseRepository.delete(existingWarehouse);
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setData("Warehouse Record deleted  successfully...");
		res.setMessage("Record got deleted ");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}

	public ResponseEntity<ResponseStructure<Warehouse>> updateWarehouse(Warehouse warehouse) {
		
		if(warehouse.getId()==null)
			throw new InvalidValidationException("Id must be passed for updation....");
		Optional<Warehouse> opt=warehouseRepository.findById(warehouse.getId());
		if(opt.isEmpty())
			throw new IdNotFoundException("Id not found int the db");
		//Existing warehouse data
		 Warehouse  existingWarehouse=opt.get();
			
		 if(warehouse.getContactNo()==null)
				throw new InvalidValidationException("Contact Number must be passed");
			
		if(String.valueOf(warehouse.getContactNo()).length()!=10)
				throw new InvalidValidationException("Contact No must be of 10 digit.......");
		
		//this logic is important 
		if( !existingWarehouse.getContactNo().equals(warehouse.getContactNo()) && warehouseRepository.existsByContactNo(warehouse.getContactNo()))
				throw new InvalidValidationException("Contact No must be unique........");
		//updating	
		warehouseRepository.save(warehouse);
		ResponseStructure<Warehouse> res=new ResponseStructure<>();
 		res.setData(warehouse);
 		res.setMessage("Record got updated Successfully");
 		res.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}
	
	
	
	
	

}

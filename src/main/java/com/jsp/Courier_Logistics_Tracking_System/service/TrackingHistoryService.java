package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.entity.TrackingHistory;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.repository.ShipmentRepository;
import com.jsp.Courier_Logistics_Tracking_System.repository.TrackingHistoryRepository;

@RestController
public class TrackingHistoryService {
	@Autowired
	private TrackingHistoryRepository  repo;
	@Autowired
	private ShipmentRepository shipmentRepo;

	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getAllTrackingHistory() {
		
		List<TrackingHistory> trackingHistories=repo.findAll();
		if(trackingHistories.isEmpty())
			throw new RecordNotAvailableException("No TrackingHistory Record is Available");
		ResponseStructure<List<TrackingHistory>> res=new ResponseStructure<>();
		res.setData(trackingHistories);
		res.setMessage("All TrackingHistory Fetched Successsfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<TrackingHistory>> getTrackingHisyoryById(Integer id) {
		
		if(id==null)
			throw new InvalidValidationException("Id must be passed....");
		Optional<TrackingHistory> opt=repo.findById(id);
		
		if(opt.isEmpty())
			throw new RecordNotAvailableException("TrackingHistory Data is Not Available with id "+id);
		TrackingHistory trackingHistory=opt.get();
		ResponseStructure<TrackingHistory> res=new ResponseStructure<>();
		res.setData(trackingHistory);
		res.setMessage("TrackingHistory Data Fetched Successfully....");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	

	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackinghistoryByTrackingNumber(
			String trackingNumber) {
		
		if(trackingNumber==null || trackingNumber.isBlank())
			throw new InvalidValidationException("TrackingNumber must be passed......");
		
		Optional<Shipment> opt=shipmentRepo.findByTrackingNo(trackingNumber);
		
		if(opt.isEmpty())
			throw new RecordNotAvailableException("No Shipment Record Available with this trackingNumber "+trackingNumber);
		Shipment  shipment  =opt.get();
		
		List<TrackingHistory> trackingHistories=repo.findByShipment(shipment);
		if(trackingHistories.isEmpty())
			throw new RecordNotAvailableException("No TrackingHistory Record is Available with trackingNumber "+trackingNumber);
		
		ResponseStructure<List<TrackingHistory>> res=new ResponseStructure<>();
		res.setData(trackingHistories);
		res.setMessage("TrackingHistory Records fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryByStatus(Status status) {
		if(status==null)
			throw new InvalidValidationException("Status must be passed.....");
		List<TrackingHistory> trackingHistories= repo.findByStatus(status);
		if(trackingHistories.isEmpty())
			throw  new RecordNotAvailableException("TrackingHistory Data Not  Available with status "+status);
		ResponseStructure<List<TrackingHistory>> res=new ResponseStructure<>();
		res.setData(trackingHistories);
		res.setMessage("TrackingHistory Records Fetched  Successfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}

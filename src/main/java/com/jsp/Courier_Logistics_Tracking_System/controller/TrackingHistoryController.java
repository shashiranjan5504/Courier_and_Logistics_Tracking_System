package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.entity.TrackingHistory;
import com.jsp.Courier_Logistics_Tracking_System.service.TrackingHistoryService;

@RestController
@RequestMapping("/trackinghistory")
public class TrackingHistoryController {
	
	@Autowired
	private TrackingHistoryService service;
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getAllTrackingHistory(){
		return service.getAllTrackingHistory();
	}
	
	@GetMapping("id/{id}")
	public ResponseEntity<ResponseStructure<TrackingHistory>> getTrackingHistoryById(@PathVariable Integer id){
		return service.getTrackingHisyoryById(id);
	}
	
	@GetMapping("trackingnumber/{trackingNumber}")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryBytrackingNumber(@PathVariable String trackingNumber){
		return service.getTrackinghistoryByTrackingNumber(trackingNumber);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryByStatus(@PathVariable Status status){
		return service.getTrackingHistoryByStatus(status);
	}

}

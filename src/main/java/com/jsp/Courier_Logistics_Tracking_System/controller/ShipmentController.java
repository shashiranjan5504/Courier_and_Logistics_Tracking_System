package com.jsp.Courier_Logistics_Tracking_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.service.ShipmentService;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
	
	@Autowired
	private ShipmentService serv;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Shipment>> createShipment(@RequestBody Shipment shipment){
		return serv.createShipment(shipment);
		
	}

}

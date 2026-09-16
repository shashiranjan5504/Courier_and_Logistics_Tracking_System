package com.jsp.Courier_Logistics_Tracking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.PackageEntity;
import com.jsp.Courier_Logistics_Tracking_System.entity.PackageType;
import com.jsp.Courier_Logistics_Tracking_System.service.PackageEntityService;

@RestController

@RequestMapping("/packageentity")
public class PackageEntityController {
	
	@Autowired
	private PackageEntityService packageEntityService;
	
	@GetMapping("/{packageType}")
	public ResponseEntity<ResponseStructure<List<PackageEntity>>> getPackageEntityByPackageType(@PathVariable PackageType packageType){
		return  packageEntityService.getPackageEntityByPackageType(packageType);
	}
	
	@GetMapping("id/{id}")
	public  ResponseEntity<ResponseStructure<PackageEntity>> getPackageEntityByPackageId(@PathVariable Integer id){
		return packageEntityService.getPackageEntityById(id);
	}
	@GetMapping()
	public ResponseEntity<ResponseStructure<List<PackageEntity>>> getAllPackageEntity(){
		return packageEntityService.getAllPackageEntity();
		
	}

}

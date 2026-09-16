package com.jsp.Courier_Logistics_Tracking_System.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.Courier_Logistics_Tracking_System.dto.ResponseStructure;
import com.jsp.Courier_Logistics_Tracking_System.entity.PackageEntity;
import com.jsp.Courier_Logistics_Tracking_System.entity.PackageType;
import com.jsp.Courier_Logistics_Tracking_System.exception.InvalidValidationException;
import com.jsp.Courier_Logistics_Tracking_System.exception.RecordNotAvailableException;
import com.jsp.Courier_Logistics_Tracking_System.repository.PackageEntityRepository;

@Service
public class PackageEntityService {
	
	@Autowired
	private  PackageEntityRepository repo;

	public ResponseEntity<ResponseStructure<List<PackageEntity>>> getPackageEntityByPackageType(
			PackageType packageType) {
		if(packageType==null)
			throw new InvalidValidationException("packageType must be passed.....");
		
		List<PackageEntity> packageEntities= repo.findByPackageType(packageType);
		if(packageEntities.isEmpty())
			throw new RecordNotAvailableException("No PackageEntity Record Available .......");
		
		ResponseStructure<List<PackageEntity>> res=new ResponseStructure<>();
		res.setData(packageEntities);
		res.setMessage("Record fetched successfully");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PackageEntity>> getPackageEntityById(Integer id) {
		
		if(id==null)
			throw new InvalidValidationException("PackageId must be passed......");
		
		Optional<PackageEntity> opt=repo.findById(id);
		if(opt.isEmpty())
			throw new RecordNotAvailableException( "No PackageEntity record available with id: " + id);
		
		PackageEntity packageEntity=opt.get();
		
		ResponseStructure<PackageEntity> res=new ResponseStructure<>();
		res.setData(packageEntity);
		res.setMessage("Record fetched Successfully");
		res.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}

	public ResponseEntity<ResponseStructure<List<PackageEntity>>> getAllPackageEntity() {
		
		List<PackageEntity> packageEntities=repo.findAll();
		if(packageEntities.isEmpty())
			throw new RecordNotAvailableException("No PackageEntity Record is Available....");
		ResponseStructure<List<PackageEntity>> res=new ResponseStructure<>();
		res.setData(packageEntities);
		res.setMessage("All Record Fetched Successfully........");
		res.setStatusCode(HttpStatus.OK.value());
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	

}

package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.Warehouse;

@Repository
public interface  WarehouseRepository extends JpaRepository <Warehouse,Integer>{

	boolean existsByContactNo(Long contactNo);

	List<Warehouse> findByLocation(String location);

	List<Warehouse> findByCapacityGreaterThan(Double capacity);

	

}

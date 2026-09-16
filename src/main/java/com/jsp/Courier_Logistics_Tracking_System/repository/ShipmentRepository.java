package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Integer> {

	boolean existsByWarehouseIdAndStatusNotAndStatusNot(Integer id, Status canceled, Status delivered);

	boolean existsByCustomerIdAndStatusNotAndStatusNot(Integer id, Status delivered, Status canceled);

	boolean existsByDeliveryAgentId(Integer id);

	boolean existsByTrackingNo(String trackingNo);

	

	Optional<Shipment> findByTrackingNo(String trackingNumber);

}

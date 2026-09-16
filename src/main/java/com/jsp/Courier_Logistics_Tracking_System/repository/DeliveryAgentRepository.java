package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.AvailableStatus;
import com.jsp.Courier_Logistics_Tracking_System.entity.DeliveryAgent;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent,Integer> {

	boolean existsByPhoneNo(Long phoneNo);

	boolean existsByVehicleNo(String vehicleNo);

	Optional<DeliveryAgent> findByVehicleNo(String vehicleNo);

	Optional<DeliveryAgent> findByPhoneNo(Long phoneNo);

	List<DeliveryAgent> findByRatingGreaterThan(Double rating);

	List<DeliveryAgent> findByAvailableStatus(AvailableStatus available);

}

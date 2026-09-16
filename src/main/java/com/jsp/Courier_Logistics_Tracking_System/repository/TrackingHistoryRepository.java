package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.Shipment;
import com.jsp.Courier_Logistics_Tracking_System.entity.Status;
import com.jsp.Courier_Logistics_Tracking_System.entity.TrackingHistory;

@Repository
public interface TrackingHistoryRepository extends  JpaRepository<TrackingHistory,Integer> {

	List<TrackingHistory> findByShipment(Shipment shipment);

	List<TrackingHistory> findByStatus(Status status);

}

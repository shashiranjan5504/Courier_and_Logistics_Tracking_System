package com.jsp.Courier_Logistics_Tracking_System.entity;





import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TrackingHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private  Integer id;
	private String location;
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	@JoinColumn
	@ManyToOne
	private Shipment shipment;
	
}

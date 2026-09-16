package com.jsp.Courier_Logistics_Tracking_System.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Shipment {
	
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique=true)
	private String trackingNo;
	
	private String source;
	
	private  String destination;
	
	private Double weight;
	
	private LocalDateTime shipmentDateTime;
	
	private LocalDate deliveryDate;
	@Enumerated(EnumType.STRING)
	private Status status;
	@JoinColumn
	@ManyToOne
	private Customer customer;
	
	@JoinColumn
	@OneToOne(cascade=CascadeType.ALL)
	private PackageEntity packageEntity;
	
	@JoinColumn
	@OneToOne(cascade=CascadeType.ALL)
	private  Payment payment;
	
	@JoinColumn
	@ManyToOne
	private DeliveryAgent deliveryAgent;
	
	@JsonIgnore
	@OneToMany(mappedBy="shipment",cascade=CascadeType.ALL)
	private List<TrackingHistory> trackingHistories;  
	
	@JoinColumn
	@ManyToOne
	private Warehouse warehouse ;
	
}

package com.jsp.Courier_Logistics_Tracking_System.entity;

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
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DeliveryAgent {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private  String name;
	@Column(unique=true)
	private Long phoneNo;
	private String vehicleNo;
	@Enumerated(EnumType.STRING)
	private AvailableStatus availableStatus;
	private Double rating;
	@JsonIgnore
	@OneToMany(mappedBy="deliveryAgent",cascade=CascadeType.ALL)
	private List<Shipment> shipments;
}

package com.jsp.Courier_Logistics_Tracking_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PackageEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private  Integer id;
	@Enumerated(EnumType.STRING)
	private PackageType packageType;
	private boolean fragile;
	private String dimensions;
	@JsonIgnore
	@OneToOne(mappedBy="packageEntity")
	private Shipment shipment;

}

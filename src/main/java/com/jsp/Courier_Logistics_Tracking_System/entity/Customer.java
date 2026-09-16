package com.jsp.Courier_Logistics_Tracking_System.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer  id;
	private String name;
	@Column(unique=true)
	private String email;
	@Column(unique=true)
	private Long phoneNo;
	private String address;
	@JsonIgnore
	@OneToMany(mappedBy="customer",cascade=CascadeType.ALL)
	private List<Shipment> shipments;

}

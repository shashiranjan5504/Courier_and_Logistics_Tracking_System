package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	boolean existsByEmail(String email);

	

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByContactNo(Long contactNo);

	boolean existsByPhoneNo(Long phoneNo);

	

}

package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.Payment;
import com.jsp.Courier_Logistics_Tracking_System.entity.PaymentMethod;

@Repository
public interface PaymentRepository extends JpaRepository <Payment,Integer> {

	List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

	

}

package com.jsp.Courier_Logistics_Tracking_System.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.Courier_Logistics_Tracking_System.entity.PackageEntity;
import com.jsp.Courier_Logistics_Tracking_System.entity.PackageType;

@Repository
public interface PackageEntityRepository extends JpaRepository<	PackageEntity,Integer> {

	List<PackageEntity> findByPackageType(PackageType packageType);

	Optional<PackageEntity> findById(Integer id);

}

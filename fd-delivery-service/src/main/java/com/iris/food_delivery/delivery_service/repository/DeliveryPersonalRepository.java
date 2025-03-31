package com.iris.food_delivery.delivery_service.repository;

import com.iris.food_delivery.delivery_service.entity.DeliveryPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPersonalRepository extends JpaRepository<DeliveryPersonal, String> {
    List<DeliveryPersonal> findByDeliveryPartnerUserName(String deliveryPartnerUserName);
}

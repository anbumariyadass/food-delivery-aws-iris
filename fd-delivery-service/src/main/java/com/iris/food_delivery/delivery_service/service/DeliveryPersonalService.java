package com.iris.food_delivery.delivery_service.service;

import com.iris.food_delivery.delivery_service.entity.DeliveryPersonal;
import com.iris.food_delivery.delivery_service.repository.DeliveryPersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryPersonalService {

    @Autowired
    private DeliveryPersonalRepository repository;

    public DeliveryPersonal save(DeliveryPersonal dp) {
        return repository.save(dp);
    }

    public Optional<DeliveryPersonal> getByUserName(String userName) {
        return repository.findById(userName);
    }

    public void deleteByUserName(String userName) {
        repository.deleteById(userName);
    }

    public List<DeliveryPersonal> getByDeliveryPartnerUserName(String deliveryPartnerUserName) {
        return repository.findByDeliveryPartnerUserName(deliveryPartnerUserName);
    }
}

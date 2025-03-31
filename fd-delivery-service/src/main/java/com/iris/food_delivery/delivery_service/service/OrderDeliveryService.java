package com.iris.food_delivery.delivery_service.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.food_delivery.delivery_service.client.RestApiClient;
import com.iris.food_delivery.delivery_service.entity.DeliveryOrderTracker;
import com.iris.food_delivery.delivery_service.entity.OrderDelivery;
import com.iris.food_delivery.delivery_service.repository.DeliveryOrderTrackerRepository;
import com.iris.food_delivery.delivery_service.repository.OrderDeliveryRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderDeliveryService {
	@Autowired
	OrderDeliveryRepository orderDeliveryRepository;
	
	@Autowired
	DeliveryOrderTrackerRepository deliveryOrderTrackerRepository;
	
	@Autowired
	RestApiClient restApiClient;
	
	public void saveOrderDelivery(OrderDelivery orderDelivery) {
		orderDeliveryRepository.save(orderDelivery);
	}
	
	public List<OrderDelivery> findOrdersByDeliveryPartner(String deliveryPartnerUserName) {
		return orderDeliveryRepository.findByDlvryPartnerUserNameOrderByOrderIdDesc(deliveryPartnerUserName);
	}
	
	public List<OrderDelivery> findNotDeliveredOrdersByDeliveryPartner(String deliveryPartnerUserName) {
		return orderDeliveryRepository.findByDlvryPartnerUserNameAndOrderStatusNotOrderByOrderIdDesc(deliveryPartnerUserName, "DELIVERED");
	}
	
	@Transactional
    public void updateOrderStatus(Long orderId, String orderStatus, String loggedInUser) {
        int updatedRows = orderDeliveryRepository.updateOrderStatus(orderId, orderStatus);
        if (updatedRows == 0) {
            throw new RuntimeException("Order not found or update failed for orderId: " + orderId);
        }
        addDeliveryTracker(orderId, orderStatus, loggedInUser);
        restApiClient.updateOrderStatus(orderId, orderStatus);
        
    }
	
	private void addDeliveryTracker(Long orderId, String orderStatus, String updatedBy) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define date format
		String formattedDate = formatter.format(new Date()); // Get current date-time
		
		DeliveryOrderTracker deliveryOrderTracker = new DeliveryOrderTracker();
		deliveryOrderTracker.setOrderId(orderId);
		deliveryOrderTracker.setOrderStatus(orderStatus);
		deliveryOrderTracker.setOrderStatusOn(formattedDate);
		deliveryOrderTracker.setOrderStatusUpdatedBy(updatedBy);
		deliveryOrderTrackerRepository.save(deliveryOrderTracker);
	}
	
	public List<DeliveryOrderTracker> findAllTrackerInfoForOrderId(Long orderId) {
		return deliveryOrderTrackerRepository.findByOrderIdOrderByTrackerIdAsc(orderId);
	}
	
	public List<OrderDelivery> getOrdersByDeliveryPersonalUserName(String deliveryPersonalUserName) {
        return orderDeliveryRepository.findByDeliveryPersonalUserNameOrderByOrderIdDesc(deliveryPersonalUserName);
    }

    public int assignDeliveryPersonal(Long orderId, String deliveryPersonalUserName) {
        return orderDeliveryRepository.updateDeliveryPersonalUserName(orderId, deliveryPersonalUserName);
    }

	
}

package com.iris.food_delivery.delivery_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iris.food_delivery.delivery_service.client.RestApiClient;
import com.iris.food_delivery.delivery_service.dto.ApiResponse;
import com.iris.food_delivery.delivery_service.entity.DeliveryOrderTracker;
import com.iris.food_delivery.delivery_service.entity.DeliveryPartner;
import com.iris.food_delivery.delivery_service.entity.DeliveryPersonal;
import com.iris.food_delivery.delivery_service.entity.OrderDelivery;
import com.iris.food_delivery.delivery_service.service.DeliveryPartnerService;
import com.iris.food_delivery.delivery_service.service.DeliveryPersonalService;
import com.iris.food_delivery.delivery_service.service.OrderDeliveryService;
import com.iris.food_delivery.delivery_service.service.UserService;

@RestController
@CrossOrigin(origins = "*")  // Allows requests from any origin
@RequestMapping("/delivery")
public class DeliveryController {
	
	@Autowired
	private RestApiClient restApiClient;
	
	@Autowired
	private OrderDeliveryService orderDeliveryService;
	
	@Autowired
	private DeliveryPartnerService deliveryPartnerService;
	
	@Autowired
	private DeliveryPersonalService deliveryPersonalService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/ping")
	public ResponseEntity<ApiResponse> ping(){
		return ResponseEntity.ok(new ApiResponse("Hello from Delivery service...", null));
	}
	
	//Receive orders from SQS
    @PostMapping("/processOrders")
    @PreAuthorize("hasAnyAuthority('ROLE_DELIVERY_PARTNER', 'ROLE_RESTAURANT')")
    public ResponseEntity<ApiResponse> receiveOrders() {
    	List<OrderDelivery> orderDeliveryList = restApiClient.pollOrderFromQueue();
    	orderDeliveryList.forEach(orderDelivery -> orderDeliveryService.saveOrderDelivery(orderDelivery));
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", null));
    }
    
    @GetMapping("/myorders")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> getAllOrdersForDeliveryPartner() {
    	List<OrderDelivery> orderDeliveryList = orderDeliveryService.findOrdersByDeliveryPartner(userService.getLoggedInUser());
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", orderDeliveryList));
    }
    
    @GetMapping("/myorders/notdelivered")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> getNotDeliveredOrdersForDeliveryPartner() {
    	List<OrderDelivery> orderDeliveryList = orderDeliveryService.findNotDeliveredOrdersByDeliveryPartner(userService.getLoggedInUser());
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", orderDeliveryList));
    }
    
    @PutMapping("/{orderId}/updateOrderStatus")
    @PreAuthorize("hasAnyAuthority('ROLE_DELIVERY_PERSONAL', 'ROLE_DELIVERY_PARTNER', 'ROLE_RESTAURANT')")
    public ResponseEntity<ApiResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String orderStatus) {
        
    	orderDeliveryService.updateOrderStatus(orderId, orderStatus, userService.getLoggedInUser());
        return ResponseEntity.ok(new ApiResponse("SUCCESS", null));
    }
    
    @GetMapping("/{orderId}/trackOrder")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponse> getTrackOrderInfo(
            @PathVariable Long orderId) {
    	List<DeliveryOrderTracker> orderDeliveryTracker = orderDeliveryService.findAllTrackerInfoForOrderId(orderId);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", orderDeliveryTracker));
    }
    
    @PostMapping("/saveDlvryPartner")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> saveDeliveryPartner(@RequestBody DeliveryPartner deliveryPartner) {
    	deliveryPartner.setUserName(userService.getLoggedInUser());
    	DeliveryPartner savedDeliveryPartner = deliveryPartnerService.saveDeliveryPartner(deliveryPartner);
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", savedDeliveryPartner));
    }
    

    @GetMapping("/getDeliveryPartner")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> getDeliveryPartner() {
    	Optional<DeliveryPartner> deliveryPartner = deliveryPartnerService.getDeliveryPartnerByUserName(userService.getLoggedInUser());
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", deliveryPartner));
    }
    
    @GetMapping("/getAllDeliveryPartners")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponse> getAllDeliveryPartners() {
    	List<DeliveryPartner> deliveryPartners = deliveryPartnerService.getAllDeliveryPartners();
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", deliveryPartners));
    }
    
    @DeleteMapping("/deleteDeliveryPartner/{deliveryPartnerUserName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteDeliveryPartner(@PathVariable String deliveryPartnerUserName ) {
    	deliveryPartnerService.deleteDeliveryPartner(deliveryPartnerUserName);
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", null));
    }
    
    @PostMapping("/saveDlvryPersonal")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> save(@RequestBody DeliveryPersonal deliveryPersonal) {
    	deliveryPersonal.setDeliveryPartnerUserName(userService.getLoggedInUser());
    	DeliveryPersonal savedDVP = deliveryPersonalService.save(deliveryPersonal);
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", savedDVP));
    }

    @GetMapping("/getDeliveryPersonal")
    public ResponseEntity<ApiResponse> getByUserName() {
    	Optional<DeliveryPersonal> deliveryPersonal = deliveryPersonalService.getByUserName(userService.getLoggedInUser());
        return ResponseEntity.ok(new ApiResponse("SUCCESS", deliveryPersonal));               
    }

    @DeleteMapping("/delete/deliveryPersonal/{userName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> deleteByUserName(@PathVariable String userName) {
    	deliveryPersonalService.deleteByUserName(userName);
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", null));      
    }

    @GetMapping("/allDeliveryPersonal")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> getByDeliveryPartnerUserName() {
    	List<DeliveryPersonal> deliveryPersonalList = deliveryPersonalService.getByDeliveryPartnerUserName(userService.getLoggedInUser());
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", deliveryPersonalList)); 
    }
    
    @GetMapping("/personal/orders")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PERSONAL')")
    public ResponseEntity<ApiResponse> getOrdersByDeliveryPersonal() {
    	List<OrderDelivery> orderForDelivery = orderDeliveryService.getOrdersByDeliveryPersonalUserName(userService.getLoggedInUser());
    	return ResponseEntity.ok(new ApiResponse("SUCCESS", orderForDelivery)); 
    }

    @PutMapping("/{orderId}/assignDeliveryPersonal")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY_PARTNER')")
    public ResponseEntity<ApiResponse> assignDeliveryPersonal(@PathVariable Long orderId,
                                                         @RequestParam String userName) {
        int updated = orderDeliveryService.assignDeliveryPersonal(orderId, userName);
        
        if (updated > 0) {
        	return ResponseEntity.ok(new ApiResponse("SUCCESS", "Delivery personal assigned successfully.")); 
        } else {
        	throw new RuntimeException("Delivery personal assignment is failed.");
        }
    }
    
}

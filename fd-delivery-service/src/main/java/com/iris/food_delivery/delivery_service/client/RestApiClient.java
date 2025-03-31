package com.iris.food_delivery.delivery_service.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.food_delivery.delivery_service.entity.OrderDelivery;
import com.iris.food_delivery.delivery_service.service.JwtService;

@Component
public class RestApiClient {
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	RestTemplate restTemplate;

	@Value("${order.poll.api.url}")
	private String orderPollQueueApiUrl;
	
	@Value("${order.status.update.api.url}")
	private String orderStatusUpdateUrl;

    public List<OrderDelivery> pollOrderFromQueue() {
    	List<OrderDelivery> orderDeliveryList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtService.getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(orderPollQueueApiUrl, HttpMethod.GET, entity, String.class);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        
        ObjectMapper objectMapper = new ObjectMapper();

        try {
        	orderDeliveryList  = objectMapper.readValue(response.getBody(), new TypeReference<List<OrderDelivery>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return orderDeliveryList;
        
    }
    
    public String updateOrderStatus(long orderId, String orderStatus) {
    	
    	System.out.println("orderId :: " + orderId + " :: orderStatus :: " + orderStatus);
    	
    	String url = String.format(orderStatusUpdateUrl, orderId, orderStatus);
    	
    	 HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", jwtService.getJwtToken());
         headers.setContentType(MediaType.APPLICATION_JSON);

         HttpEntity<String> entity = new HttpEntity<>(headers);

         ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

         System.out.println("Response Status Code: " + response.getStatusCode());
         System.out.println("Response Body: " + response.getBody());
         return response.getBody();
    }


}

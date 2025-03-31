package com.iris.food_delivery.order_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.food_delivery.order_service.entity.Order;
import com.iris.food_delivery.order_service.service.JwtService;

@Component
public class RestApiClient {
	
	@Autowired
	JwtService jwtService;

	@Value("${order.push.api.url}")
	private String orderPushQueueApiUrl;

    public String pushOrderIntoQueue(Order order) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtService.getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

     // Convert Order object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrder;
        try {
            jsonOrder = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error: Unable to convert Order to JSON";
        }

        // Create the request with the JSON body
        HttpEntity<String> entity = new HttpEntity<>(jsonOrder, headers);

        ResponseEntity<String> response = restTemplate.exchange(orderPushQueueApiUrl, HttpMethod.POST, entity, String.class);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        return response.getStatusCode() + " : " + response.getBody();
    }

}

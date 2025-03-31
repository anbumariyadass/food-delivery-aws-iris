package com.iris.food_delivery.delivery_service.client;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.food_delivery.delivery_service.dto.ApiResponseGeneric;
import com.iris.food_delivery.delivery_service.entity.OrderDelivery;

public class Test {
	 public static void main(String[] args) throws Exception {
	        String jsonResponse = "[ { \"orderId\":13, \"dlvryPartnerUserName\":\"xyz\", \"dlvryPartnerName\":\"Fast Delivery\", \"totalPrice\":45.75, \"customerContactName\":\"John Doe\", \"customerContactAddress\":\"123 Main Street, City, State\", \"customerContactEmail\":\"john.doe@example.com\", \"customerContactPhone\":\"9876543210\", \"restaurantName\":\"Tasty Bites\", \"orderStatus\":\"ORDERED\" }, { \"orderId\":12, \"dlvryPartnerUserName\":\"xyz\", \"dlvryPartnerName\":\"Fast Delivery\", \"totalPrice\":45.75, \"customerContactName\":\"John Doe\", \"customerContactAddress\":\"123 Main Street, City, State\", \"customerContactEmail\":\"john.doe@example.com\", \"customerContactPhone\":\"9876543210\", \"restaurantName\":\"Tasty Bites\", \"orderStatus\":\"ORDERED\" } ] ";

	        ObjectMapper objectMapper = new ObjectMapper();

	        try {
	        	List<OrderDelivery> orderDeliveryList = objectMapper.readValue(jsonResponse, new TypeReference<List<OrderDelivery>>() {});
	        	System.out.println(orderDeliveryList.size());
	        	orderDeliveryList.forEach(o->System.out.println(o.getOrderId()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}

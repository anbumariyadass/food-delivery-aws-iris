package com.iris.food_delivery.cart_service.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import java.util.List;

@DynamoDbBean
public class Cartfd {

    private String customerUserName;
    private String restaurantUserName;
    private String restaurantName;
    private List<Dish> dishes;

    // Default constructor (required by DynamoDB SDK)
    public Cartfd() {}

    @DynamoDbPartitionKey
    @DynamoDbAttribute("customerUserName")
    public String getCustomerUserName() {
        return customerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        this.customerUserName = customerUserName;
    }

    @DynamoDbAttribute("restaurantUserName")
    public String getRestaurantUserName() {
        return restaurantUserName;
    }

    public void setRestaurantUserName(String restaurantUserName) {
        this.restaurantUserName = restaurantUserName;
    }

    @DynamoDbAttribute("restaurantName")
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @DynamoDbAttribute("dishes")
    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}



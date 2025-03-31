package com.iris.food_delivery.delivery_service.dto;

public class ApiResponseGeneric<T> {
    private String message;
    private T data;

    public ApiResponseGeneric(String message, T data) {
        this.message = message;
        this.data = data;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
    
}


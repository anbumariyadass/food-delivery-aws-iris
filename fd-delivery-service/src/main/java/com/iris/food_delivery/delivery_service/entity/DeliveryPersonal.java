package com.iris.food_delivery.delivery_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DELIVERY_PERSONAL")
public class DeliveryPersonal {

    @Id
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName; // Primary key

    @Column(name = "DLVRY_PERSONAL_NAME", nullable = false)
    private String deliveryPersonalName;
    
    @Column(name = "DLVRY_PARTNER_USER_NAME", nullable = false)
    private String deliveryPartnerUserName;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;
    
    @Column(name = "GENDER", nullable = false)
    private String gender;


    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getDeliveryPersonalName() {
		return deliveryPersonalName;
	}

	public void setDeliveryPersonalName(String deliveryPersonalName) {
		this.deliveryPersonalName = deliveryPersonalName;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDeliveryPartnerUserName() {
		return deliveryPartnerUserName;
	}

	public void setDeliveryPartnerUserName(String deliveryPartnerUserName) {
		this.deliveryPartnerUserName = deliveryPartnerUserName;
	}
	
	
    
}

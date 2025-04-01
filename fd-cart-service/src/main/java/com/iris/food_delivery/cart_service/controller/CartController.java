package com.iris.food_delivery.cart_service.controller;

import com.iris.food_delivery.cart_service.dto.ApiResponse;
import com.iris.food_delivery.cart_service.entity.Cartfd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.iris.food_delivery.cart_service.service.CartService;
import com.iris.food_delivery.cart_service.service.UserService;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/ping")
	public ResponseEntity<ApiResponse> welcome() {
		return ResponseEntity.ok(new ApiResponse("Hello from Cart Service...", null));
	}

	@PostMapping("/save")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public ResponseEntity<ApiResponse> addCart(@RequestBody Cartfd cart) {
		Cartfd addedCart = cartService.saveCart(cart);
		return ResponseEntity.ok(new ApiResponse("Cart is saved successfully", addedCart));
	}

	@GetMapping("/mycart")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public ResponseEntity<ApiResponse> getCart() {
		Cartfd cart = cartService.getCart(userService.getLoggedInUser());
		return ResponseEntity.ok(new ApiResponse("Cart is retrieved successfully", cart));
	}


	@DeleteMapping("/mycart")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public ResponseEntity<ApiResponse> deleteCart() {
		cartService.deleteCart(userService.getLoggedInUser());
		return ResponseEntity.ok(new ApiResponse("Cart is deleted successfully", null));
	}
}

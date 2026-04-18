package com.aptechproject.babyshop.constant;

public final class AppConstants {

    private AppConstants() {}

    // --- ERROR MESSAGES ---
    public static final String ERROR_USER_ALREADY_EXISTS = "You are already registered";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ERROR_IMAGE_UPLOAD = "Failed to upload image to Supabase";
    public static final String ERROR_PRODUCT_INVALID = "Product cannot be found";
    public static final String ERROR_CART_INVALID = "There was an error, your cart does not exists";
    public static final String ERROR_CART_EMPTY = "Cart is empty!";
    public static final String ERROR_STOCK_OVERDRAFT = "Not enough stock available!";
    public static final String ERROR_INVALID_TOKEN = "Invalid / expired token!";
    

    // --- VALIDATION MESSAGES ---
    public static final String VALIDATION_NAME_EMPTY = "Name cannot be empty";
    public static final String VALIDATION_EMAIL_EMPTY = "Email cannot be empty";
    public static final String VALIDATION_EMAIL_INVALID = "Must be a valid email format";
    public static final String VALIDATION_PASSWORD_EMPTY = "Password cannot be empty";
    public static final String VALIDATION_QUANTITY_EMPTY = "Quantity cannot be null";
    public static final String VALIDATION_PRICE_EMPTY = "Price cannot be null";

    // --- API ENDPOINTS ---
    public static final String API_BASE = "/api";
    public static final String API_USERS = API_BASE + "/users";
    public static final String API_REGISTER = "/register";
    public static final String API_LOGIN = "/login";
    public static final String API_ME = "/me";
    public static final String API_PRODUCTS = API_BASE + "/products";
    public static final String API_SERVICE_ADD = "/add";
    public static final String API_CART = API_BASE + "/cart";
    public static final String API_CHECKOUT = "/checkout";

    // --- SUCCESS MESSAGES ---
    public static final String VALIDATION_CHECKOUT_SUCCESSFUL= "Checkout successful! Thank you for shopping.";
}
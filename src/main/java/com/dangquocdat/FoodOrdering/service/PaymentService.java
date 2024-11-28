package com.dangquocdat.FoodOrdering.service;

import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.payment.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentResponse createPaymentLink(OrderResponse order) throws StripeException;
}

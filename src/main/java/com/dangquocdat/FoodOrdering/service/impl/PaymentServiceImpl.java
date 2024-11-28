package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.order.response.OrderResponse;
import com.dangquocdat.FoodOrdering.dto.payment.PaymentResponse;
import com.dangquocdat.FoodOrdering.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.secret-key}")
    private String stripeApiSecretKey;

    @Override
    public PaymentResponse createPaymentLink(OrderResponse order) throws StripeException {

        Stripe.apiKey = stripeApiSecretKey;

        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(
                                            SessionCreateParams.PaymentMethodType.CARD
                                    )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment/success/"+order.getId())
                .setCancelUrl("http://localhost:5173/payment/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                    .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount((long) order.getTotalPrice() * 100)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Food Ordering")
                                .build()
                            ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        PaymentResponse response = new PaymentResponse(session.getUrl());

        return response;
    }
}

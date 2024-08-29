package com.fatichdev.ecommerce.payment;

import com.fatichdev.ecommerce.customer.CustomerResponse;
import com.fatichdev.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}

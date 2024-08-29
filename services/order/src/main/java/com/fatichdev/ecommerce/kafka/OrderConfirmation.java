package com.fatichdev.ecommerce.kafka;

import com.fatichdev.ecommerce.customer.CustomerResponse;
import com.fatichdev.ecommerce.order.PaymentMethod;
import com.fatichdev.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}

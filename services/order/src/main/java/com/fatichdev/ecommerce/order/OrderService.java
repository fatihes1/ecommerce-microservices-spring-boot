package com.fatichdev.ecommerce.order;

import com.fatichdev.ecommerce.customer.CustomerClient;
import com.fatichdev.ecommerce.exception.BusinessException;
import com.fatichdev.ecommerce.kafka.OrderConfirmation;
import com.fatichdev.ecommerce.kafka.OrderProducer;
import com.fatichdev.ecommerce.orderline.OrderLineRequest;
import com.fatichdev.ecommerce.orderline.OrderLineService;
import com.fatichdev.ecommerce.payment.PaymentClient;
import com.fatichdev.ecommerce.payment.PaymentRequest;
import com.fatichdev.ecommerce.product.ProductClient;
import com.fatichdev.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(@Valid OrderRequest request) {
        // Check the customer --> customer microservice (OpenFeign)
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exist with the provided ID:: " + request.customerId()));

        // Purchase the products --> product microservice (RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // Persist order

        var order = this.repository.save(mapper.toOrder(request));

        // Persist order lines
        for (PurchaseRequest purchaseRequest: request.products()) {
            this.orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //Start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);


        // Send the order confirmation --> notification microservice (via kafka message broker)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }


    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with id %d", orderId)));
    }
}

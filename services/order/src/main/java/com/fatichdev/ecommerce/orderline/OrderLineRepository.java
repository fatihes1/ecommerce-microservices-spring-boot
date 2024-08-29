package com.fatichdev.ecommerce.orderline;

import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(Integer orderId);
}

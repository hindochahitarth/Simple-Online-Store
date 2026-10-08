package org.example.simpleonlinestore.service.impl.order;

import lombok.RequiredArgsConstructor;
import org.example.simpleonlinestore.entity.CartItem;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(3)
@RequiredArgsConstructor
public class StockAndPricingHandler implements OrderHandler {
    private final ProductRepository productRepository;

    @Override
    public void handle(OrderContext context) {
        List<OrderItem> orderItemList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : context.getCart().getItems()) {
            Product product = cartItem.getProduct();

            // 1. Stock Check & Update
            if (product.getStockCount() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient Stock");
            }
            product.setStockCount(product.getStockCount() - cartItem.getQuantity());
            productRepository.save(product);

            // 2. Dynamic Price Calculation
            long calculatedPrice = product.getPrice();
            if (product.getDiscountPercentage() != null && product.getDiscountPercentage() > 0) {
                long discountAmount = (product.getPrice() * product.getDiscountPercentage()) / 100;
                calculatedPrice = product.getPrice() - discountAmount;
            }

            BigDecimal price = BigDecimal.valueOf(calculatedPrice);
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(price)
                    .build();

            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItemList.add(orderItem);
        }

        context.setOrderItems(orderItemList);
        context.setTotalAmount(totalAmount);
    }
}


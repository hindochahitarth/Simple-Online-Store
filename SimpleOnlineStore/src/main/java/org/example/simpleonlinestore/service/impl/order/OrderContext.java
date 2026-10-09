package org.example.simpleonlinestore.service.impl.order;
import lombok.Builder;
import lombok.Data;
import org.example.simpleonlinestore.entity.Address;
import org.example.simpleonlinestore.entity.Cart;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.entity.User;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class OrderContext {
    private final Long addressId;
    private User user;
    private Address address;
    private Cart cart;
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    private String razorpayOrderId;
}
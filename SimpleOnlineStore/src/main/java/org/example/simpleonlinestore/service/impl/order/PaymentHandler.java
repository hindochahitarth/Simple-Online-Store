package org.example.simpleonlinestore.service.impl.order;

import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.example.simpleonlinestore.service.interfaces.PaymentStrategy;
import org.example.simpleonlinestore.service.interfaces.RazorpayService;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(4)
@RequiredArgsConstructor
public class PaymentHandler implements OrderHandler {

    private final List<PaymentStrategy> paymentStrategies;
    @Override
    public void handle(OrderContext context) {

        String receiptId = "txn_" + System.currentTimeMillis();
        PaymentStrategy strategy=paymentStrategies.stream()
                .filter(s -> s.getProviderName().equalsIgnoreCase("RAZORPAY"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment provider not configured"));

        Map<String,Object> orderResponse= strategy.createOrder(
                context.getTotalAmount().doubleValue(),
                receiptId
        );
        context.setRazorpayOrderId((String) orderResponse.get("id"));

    }
}


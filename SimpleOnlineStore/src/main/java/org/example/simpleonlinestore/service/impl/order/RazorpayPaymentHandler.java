package org.example.simpleonlinestore.service.impl.order;

import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.example.simpleonlinestore.service.interfaces.OrderHandler;
import org.example.simpleonlinestore.service.interfaces.RazorpayService;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@RequiredArgsConstructor
public class RazorpayPaymentHandler implements OrderHandler {
    private final RazorpayService razorpayService;

    @Override
    public void handle(OrderContext context) {
        try {
            String receiptId = "txn_" + System.currentTimeMillis();
            JSONObject razorpayOrderJson = razorpayService.createOrder(
                    context.getTotalAmount().doubleValue(),
                    receiptId
            );
            context.setRazorpayOrderId(razorpayOrderJson.getString("id"));
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to generate gateway token: " + e.getMessage());
        }
    }
}


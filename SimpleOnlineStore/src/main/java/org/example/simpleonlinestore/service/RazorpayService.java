package org.example.simpleonlinestore.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws RazorpayException {
        client = new RazorpayClient(keyId, keySecret);
    }

    public JSONObject createOrder(Double amount, String receipt) throws RazorpayException {
        long amountInPaise = Math.round(amount * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);

        // Create Razorpay order
        Order order = client.orders.create(orderRequest);

        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.get("id").toString());
        orderJson.put("amount", amountInPaise);
        orderJson.put("currency", "INR");

        return orderJson;
    }
    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            // Validates attributes using the static verification utility alongside your properties secret
            return Utils.verifyPaymentSignature(options, keySecret);
        } catch (Exception e) {
            return false;
        }
    }
    public String getKeyId() {
        return keyId;
    }
}

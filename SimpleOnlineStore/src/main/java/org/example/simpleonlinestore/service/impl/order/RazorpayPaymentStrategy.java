package org.example.simpleonlinestore.service.impl.order;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.annotation.PostConstruct;
import org.example.simpleonlinestore.service.interfaces.PaymentStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class RazorpayPaymentStrategy implements PaymentStrategy {
    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private RazorpayClient client;

    @PostConstruct
    // Initialize the Razorpay client after the service
    // waits until key and secret key are fully injected
    public void init() throws RazorpayException {
        client = new RazorpayClient(keyId, keySecret);
    }
    @Override
    public String getProviderName() {
        return "RAZORPAY";
    }

    @Override
    public Map<String, Object> createOrder(Double amount, String receipt) {
        try{
            long amountInPaise = Math.round(amount * 100);
            // Razorpay requires specific data fields (amount, currency, tracking receipt) to initialize an order.
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);

            // Create Razorpay order
            //makes network call
            Order order = client.orders.create(orderRequest);
            // The Razorpay SDK function (client.orders.create) is strictly designed
            Map<String,Object> response=new HashMap<>();
            response.put("id", order.get("id").toString());
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            return response;
        }
        catch (RazorpayException e){
            throw new RuntimeException("Razorpay order failed ");
        }
    }

    @Override
    public boolean verifySignature(Map<String, String> paymentDetails) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", paymentDetails.get("razorpayOrderId"));
            options.put("razorpay_payment_id", paymentDetails.get("razorpayPaymentId"));
            options.put("razorpay_signature", paymentDetails.get("razorpaySignature"));

            // Validates attributes using the static verification utility alongside your properties secret
            return Utils.verifyPaymentSignature(options, keySecret);
        } catch (Exception e) {
            return false;
        }
    }
}

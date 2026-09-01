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
    // Initialize the Razorpay client after the service
    // waits until key and secret key are fully injected
    public void init() throws RazorpayException {
        client = new RazorpayClient(keyId, keySecret);
    }
    //create a new order
    public JSONObject createOrder(Double amount, String receipt) throws RazorpayException {
      //  Razorpay  only reads the lowest currency unit.

        long amountInPaise = Math.round(amount * 100);
        // Razorpay requires specific data fields (amount, currency, tracking receipt) to initialize an order.
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);

        // Create Razorpay order
        //makes network call
        Order order = client.orders.create(orderRequest);
        // JSONObject HERE: The Razorpay SDK function (client.orders.create) is strictly designed
        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.get("id").toString());
        orderJson.put("amount", amountInPaise);
        orderJson.put("currency", "INR");

        return orderJson;
    }
    //  security check
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

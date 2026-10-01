package org.example.service.interfaces;

import com.razorpay.RazorpayException;
import org.json.JSONObject;

public interface RazorpayService {
    JSONObject createOrder(Double amount, String receipt) throws RazorpayException;
    boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);
    String getKeyId();
}

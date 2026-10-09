package org.example.simpleonlinestore.service.interfaces;
import java.util.Map;

public interface PaymentStrategy {

    String getProviderName();
    Map<String,Object> createOrder(Double amount, String receipt);

    boolean verifySignature(Map<String,String> paymentDetails);
}
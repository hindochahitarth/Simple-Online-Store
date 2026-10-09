package org.example.simpleonlinestore.service.interfaces;
import org.example.simpleonlinestore.entity.Order;

public interface InvoiceGenerator {
    String generate(Order order);
}
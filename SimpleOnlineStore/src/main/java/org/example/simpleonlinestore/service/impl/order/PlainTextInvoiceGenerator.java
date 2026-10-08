package org.example.simpleonlinestore.service.impl.order;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;

import java.math.BigDecimal;

public class PlainTextInvoiceGenerator implements InvoiceGenerator {
    @Override
    public String generate(Order order) {
        StringBuilder invoice = new StringBuilder();
        invoice.append("=== INVOICE FOR ORDER ID: ").append(order.getId()).append(" ===\n");
        invoice.append("Customer Name: "+order.getUser().getFirstName());
        invoice.append(order.getUser().getLastName());
        invoice.append("\nStatus: ").append(order.getStatus()).append("\n");
        invoice.append("Deliver To: ").append(order.getAddress().getAddressLine1());
        invoice.append(" "+order.getAddress().getAddressLine2());
        invoice.append(" "+order.getAddress().getCity())
                .append("\n\n");
        for (OrderItem item : order.getItems()) {
            invoice.append("- ")
                    .append(item.getProduct().getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" = INR ")
                    .append(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .append("\n"+item.getProduct().getDescription())
                    .append("\n");
        }
        invoice.append("\nTotal : INR ").append(order.getTotalAmount());
        return invoice.toString();
    }
}

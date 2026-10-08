package org.example.simpleonlinestore.service.impl.order;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;

import java.math.BigDecimal;

public class TaxInvoiceDecorator extends InvoiceDecorator{

    protected TaxInvoiceDecorator(InvoiceGenerator decoratedGenerator) {
        super(decoratedGenerator);
    }
    public String generate(Order order){
        String baseInvoice=super.generate(order);

        BigDecimal gstAmount= order.getTotalAmount().add(order.getTotalAmount().multiply(BigDecimal.valueOf(0.18)));

        return baseInvoice+"\n Included 18% GST  Total :"+gstAmount;

    }
}

package org.example.simpleonlinestore.service.impl.order;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;

public class HtmlInvoiceDecorator extends InvoiceDecorator{

    public HtmlInvoiceDecorator(InvoiceGenerator decoratedGenerator) {
        super(decoratedGenerator);
    }
    public String generate(Order order){
        String plainContent= super.generate(order);

        return "<div style='font-family:sans-serif;border:3px solid #333;'>"+plainContent.replace("\n","<br/>")+"</div>";
    }
}

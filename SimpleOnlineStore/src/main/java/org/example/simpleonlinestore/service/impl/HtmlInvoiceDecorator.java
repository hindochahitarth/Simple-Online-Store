package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;

public class HtmlInvoiceDecorator extends InvoiceDecorator{

    public HtmlInvoiceDecorator(InvoiceGenerator decoratedGenerator) {
        super(decoratedGenerator);
    }
    public String generate(Order order){
        String plainContent= super.generate(order);

        return "<div style='font-family:sans-serif;border:1px solid;'>"+plainContent.replace("\n","<br/>")+"</div>";
    }
}

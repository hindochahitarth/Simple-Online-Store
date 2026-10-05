package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.service.interfaces.InvoiceGenerator;

public abstract class InvoiceDecorator implements InvoiceGenerator {
    protected final InvoiceGenerator decoratedGenerator;

    protected InvoiceDecorator(InvoiceGenerator decoratedGenerator) {
        this.decoratedGenerator = decoratedGenerator;
    }
    public String generate(Order order){
        return decoratedGenerator.generate(order);
    }
}

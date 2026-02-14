package com.airtribe.meditrack.services;

import com.airtribe.meditrack.services.paymentservices.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentService(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public boolean processPayment(String type, double amount) {

        //here the type can be "card", "upi", "netbanking" etc. based on the implementations of PaymentStrategy i.e CardPayment, UpiPayment etc. classes
        //component annotation in those classes will have the same name as the type here to be able to autowire them in the map
        //the beans or objects of those classes will be automatically injected into the map by Spring based on the component annotation and the type key
        PaymentStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment type");
        }

        return strategy.processPayment(amount);
    }


}

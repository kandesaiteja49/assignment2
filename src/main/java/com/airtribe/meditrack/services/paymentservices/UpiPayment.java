package com.airtribe.meditrack.services.paymentservices;

import org.springframework.stereotype.Component;

@Component("upi")
public class UpiPayment implements PaymentStrategy {

    @Override
    public Boolean processPayment(double amount) {
        // Implement UPI payment processing logic here
        System.out.println("Processing UPI payment of amount: " + amount);
        return true;
    }

}

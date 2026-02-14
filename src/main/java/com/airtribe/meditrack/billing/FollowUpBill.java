package com.airtribe.meditrack.billing;

public class FollowUpBill extends Bill {

    public FollowUpBill(double baseAmount) {
        super(baseAmount, 0.10);
    }

    @Override
    public double getFinalAmount() {
        return baseAmount + calculateTax() - 100; // discount
    }

    @Override
    public String getBillName() {
        return "Follow-up Bill";
    }
}


package com.airtribe.meditrack.billing;

public class LabTestBill extends Bill {

    public LabTestBill(double baseAmount) {
        super(baseAmount, 0.15);
    }

    @Override
    public double getFinalAmount() {
        return baseAmount + calculateTax() + 200; // lab fee
    }

    @Override
    public String getBillName() {
        return "Lab Test Bill";
    }
}


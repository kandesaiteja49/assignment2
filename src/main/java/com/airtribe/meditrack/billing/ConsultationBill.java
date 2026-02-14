package com.airtribe.meditrack.billing;

public class ConsultationBill extends Bill {

    public ConsultationBill(double baseAmount) {
        super(baseAmount, 0.18);
    }

    @Override
    public double getFinalAmount() {
        return baseAmount + calculateTax();
    }

    @Override
    public String getBillName() {
        return "Consultation Bill";
    }
}


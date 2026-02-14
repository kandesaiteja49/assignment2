package com.airtribe.meditrack.billing;


public abstract class Bill {

    protected double baseAmount;
    protected double tax;

    public double getBaseAmount() {
        return baseAmount;
        }

    public double getTax() {
        return tax;
    }

    public Bill(double baseAmount, double tax) {
        this.baseAmount = baseAmount;
        this.tax = tax;
    }

    protected double calculateTax() {
        return baseAmount * tax;
    }

    public abstract double getFinalAmount();

    public abstract String getBillName();


}


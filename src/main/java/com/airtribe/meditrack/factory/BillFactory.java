package com.airtribe.meditrack.factory;

import com.airtribe.meditrack.billing.*;
import com.airtribe.meditrack.enums.BillType;

public class BillFactory {

    private BillFactory() {}

    public static Bill createBill(BillType billType, double amount) {

        return switch (billType) {
            case CONSULTATION -> new ConsultationBill(amount);
            case FOLLOW_UP -> new FollowUpBill(amount);
            case LAB_TEST -> new LabTestBill(amount);
        };
    }
}

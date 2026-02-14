package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.BillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {


    private String paymentType;

    private Double paymentAmount;

    private BillType billType;

}

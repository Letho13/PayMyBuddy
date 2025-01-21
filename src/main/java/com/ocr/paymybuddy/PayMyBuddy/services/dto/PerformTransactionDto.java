package com.ocr.paymybuddy.PayMyBuddy.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PerformTransactionDto {

    private String emailBeneficiary;
    private BigDecimal amount;
    private String description;

}

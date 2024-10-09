package org.sid.sgbankbackend.dto;

import lombok.Data;

@Data
public class WithdrawDTO {
    private String accountId;
    private double amount;
    private String description;
}

package org.sid.sgbankbackend.dto;

import lombok.Data;

@Data
public class DepositDTO {

    private String accountId;
    private double amount;
    private String description;

}

package org.sid.sgbankbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private List<AccountOperationDTO> accountOperationDTOS;
}

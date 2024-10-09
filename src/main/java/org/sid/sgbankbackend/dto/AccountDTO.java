package org.sid.sgbankbackend.dto;

import lombok.Data;
import org.sid.sgbankbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class AccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
}

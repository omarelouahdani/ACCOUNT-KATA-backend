package org.sid.sgbankbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.sgbankbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private Customer customer;
    private List<AccountOperation> accountOperations;
}
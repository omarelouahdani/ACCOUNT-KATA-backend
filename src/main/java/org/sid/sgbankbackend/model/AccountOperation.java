package org.sid.sgbankbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.sgbankbackend.enums.OperationType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private Account account;
    private String description;
}

package org.sid.sgbankbackend.service;

import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.model.Customer;

import java.util.List;

/**
 * Service interface for initializing account-related data.
 */
public interface DataAccountService {

    /**
     * Initializes data for customers, accounts, and account operations.
     *
     * @param customers         The list of customers to initialize.
     * @param accounts          The list of accounts to initialize.
     * @param accountOperations The list of account operations to initialize.
     */
    void initData(List<Customer> customers, List<Account> accounts, List<AccountOperation> accountOperations);
}

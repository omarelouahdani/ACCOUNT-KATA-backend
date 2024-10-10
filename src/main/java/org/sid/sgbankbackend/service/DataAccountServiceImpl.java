package org.sid.sgbankbackend.service;

import org.sid.sgbankbackend.enums.AccountStatus;
import org.sid.sgbankbackend.enums.OperationType;
import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the DataAccountService interface for initializing account-related data.
 */
@Service
public class DataAccountServiceImpl implements DataAccountService {

    /**
     * Initializes data for customers, accounts, and account operations.
     *
     * @param customers         The list to populate with customer data.
     * @param accounts          The list to populate with account data.
     * @param accountOperations The list to populate with account operation data.
     */
    @Override
    public void initData(List<Customer> customers, List<Account> accounts, List<AccountOperation> accountOperations) {
        // Array of customer names for initialization
        String[] names = {"Nicolas", "Matthieu", "Sandra"};

        for (String name : names) {
            // Create and initialize customer
            Customer customer = createCustomer(name);
            customers.add(customer);

            // Create and initialize account
            Account account = createAccount(name, customer);
            accounts.add(account);

            // Create and initialize account operations
            initializeAccountOperations(account, accountOperations);
        }
    }

    /**
     * Creates and initializes a new customer.
     *
     * @param name The name of the customer.
     * @return The initialized Customer object.
     */
    private Customer createCustomer(String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(name.toLowerCase() + "@gmail.com"); // Use lowercase for email
        return customer;
    }

    /**
     * Creates and initializes a new account associated with a customer.
     *
     * @param name     The name used for the account ID.
     * @param customer The associated customer.
     * @return The initialized Account object.
     */
    private Account createAccount(String name, Customer customer) {
        Account account = new Account();
        account.setId(name + "IBAN");
        account.setBalance(Math.random() * 100); // Random balance between 0 and 100
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>()); // Initialize the account operations list
        return account;
    }

    /**
     * Initializes account operations for a given account and adds them to the provided list.
     *
     * @param account           The account to which operations are added.
     * @param accountOperations The list to populate with account operation data.
     */
    private void initializeAccountOperations(Account account, List<AccountOperation> accountOperations) {
        for (int i = 0; i < 5; i++) {
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setId((long) (i + 1)); // Example ID
            accountOperation.setOperationDate(new Date());
            accountOperation.setAmount(Math.random() * 200); // Random amount between 0 and 200
            accountOperation.setType(Math.random() > 0.5 ? OperationType.DEPOSIT : OperationType.WITHDRAW);
            accountOperation.setAccount(account);
            account.getAccountOperations().add(accountOperation); // Add operation to the account's list
            accountOperations.add(accountOperation); // Add operation to the overall list
        }
    }
}

package org.sid.sgbankbackend.initialization;

import org.sid.sgbankbackend.enums.AccountStatus;
import org.sid.sgbankbackend.enums.OperationType;
import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.model.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class responsible for initializing data in memory.
 * This class creates customers, accounts, and account operations without persisting them in the database.
 */
public class MemoryDataInitializer {

    public List<Customer> initializeCustomers() {
        List<Customer> customers = new ArrayList<>();
        String[] names = {"Nicolas", "Matthieu", "Sandra"};

        for (String name : names) {
            Customer customer = createCustomer(name);
            customers.add(customer);
        }

        return customers;
    }

    public List<Account> initializeAccounts(List<Customer> customers) {
        List<Account> accounts = new ArrayList<>();

        for (Customer customer : customers) {
            Account account = createAccount(customer);
            accounts.add(account);
        }

        return accounts;
    }

    public void initializeAccountOperations(Account account, List<AccountOperation> accountOperations) {
        for (int i = 0; i < 5; i++) {
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setOperationDate(new Date());
            accountOperation.setAmount(Math.random() * 200);
            accountOperation.setType(Math.random() > 0.5 ? OperationType.DEPOSIT : OperationType.WITHDRAW);
            accountOperation.setAccount(account);
            account.getAccountOperations().add(accountOperation);
            accountOperations.add(accountOperation);
        }
    }

    private Customer createCustomer(String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(name.toLowerCase() + "@gmail.com");
        return customer;
    }

    private Account createAccount(Customer customer) {
        Account account = new Account();
        account.setId(customer.getName() + "IBAN");
        account.setBalance(Math.random() * 100);
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>());
        return account;
    }
}

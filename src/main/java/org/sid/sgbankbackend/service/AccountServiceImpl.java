package org.sid.sgbankbackend.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sid.sgbankbackend.dto.AccountHistoryDTO;
import org.sid.sgbankbackend.dto.AccountOperationDTO;
import org.sid.sgbankbackend.enums.OperationType;
import org.sid.sgbankbackend.exceptions.BalanceNotSufficientException;
import org.sid.sgbankbackend.exceptions.AccountNotFoundException;
import org.sid.sgbankbackend.mapper.AccountMapperImpl;
import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the AccountService interface, handling account-related operations.
 */
@Service
@AllArgsConstructor
@Data
@Slf4j
public class AccountServiceImpl implements AccountService {

    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<AccountOperation> accountOperations = new ArrayList<>();
    private DataAccountService dataAccountService;
    private AccountMapperImpl dtoMapper;

    @Override
    public void deposit(String accountId, double amount, String description) throws AccountNotFoundException {
        Account bankAccount = findAccountById(accountId);
        AccountOperation accountOperation = createAccountOperation(OperationType.DEPOSIT, amount, description, bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccount.getAccountOperations().add(accountOperation);
    }

    @Override
    public void withdraw(String accountId, double amount, String description) throws BalanceNotSufficientException, AccountNotFoundException {
        Account bankAccount = findAccountById(accountId);
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = createAccountOperation(OperationType.WITHDRAW, amount, description, bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccount.getAccountOperations().add(accountOperation);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId) throws AccountNotFoundException {
        Account bankAccount = findAccountById(accountId);
        List<AccountOperationDTO> accountOperationDTOS = bankAccount.getAccountOperations()
                .stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        return accountHistoryDTO;
    }

    @Override
    public void initData() {
        dataAccountService.initData(customers, accounts, accountOperations);
    }

    /**
     * Finds an account by its ID.
     *
     * @param accountId The ID of the account to find.
     * @return The found Account.
     * @throws AccountNotFoundException If no account is found with the given ID.
     */
    private Account findAccountById(String accountId) throws AccountNotFoundException {
        return accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    /**
     * Creates a new account operation.
     *
     * @param operationType The type of the operation (DEPOSIT or WITHDRAW).
     * @param amount        The amount of the operation.
     * @param description   A description of the operation.
     * @param bankAccount   The account associated with the operation.
     * @return The created AccountOperation.
     */
    private AccountOperation createAccountOperation(OperationType operationType, double amount, String description, Account bankAccount) {
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(operationType);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAccount(bankAccount);
        return accountOperation;
    }
}

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
import org.sid.sgbankbackend.repositories.AccountOperationRepository;
import org.sid.sgbankbackend.repositories.AccountRepository;
import org.springframework.stereotype.Service;

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
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private AccountMapperImpl dtoMapper;

    @Override
    public void deposit(String accountId, double amount, String description) throws AccountNotFoundException {
        Account bankAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation = createAccountOperation(OperationType.DEPOSIT, amount, description, bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccount.getAccountOperations().add(accountOperation);
        accountRepository.save(bankAccount);
    }

    @Override
    public void withdraw(String accountId, double amount, String description) throws BalanceNotSufficientException, AccountNotFoundException {
        Account bankAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = createAccountOperation(OperationType.WITHDRAW, amount, description, bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccount.getAccountOperations().add(accountOperation);
        accountRepository.save(bankAccount);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId) throws AccountNotFoundException {
        Account bankAccount = accountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new AccountNotFoundException("Account not Found");
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

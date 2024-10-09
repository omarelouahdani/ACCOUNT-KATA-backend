package org.sid.sgbankbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sid.sgbankbackend.dto.AccountHistoryDTO;
import org.sid.sgbankbackend.dto.AccountOperationDTO;
import org.sid.sgbankbackend.exceptions.AccountNotFoundException;
import org.sid.sgbankbackend.exceptions.BalanceNotSufficientException;
import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.enums.OperationType;
import org.sid.sgbankbackend.mapper.AccountMapperImpl;
import org.sid.sgbankbackend.model.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountMapperImpl dtoMapper;
    private List<Customer> customers;
    private List<Account> accounts;
    private List<AccountOperation> accountOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize lists
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        accountOperations = new ArrayList<>();

        // Mock the data
        initMockData();
    }

    @Test
    void testWithdrawSuccessful() throws AccountNotFoundException, BalanceNotSufficientException {
        String accountId = "NicolasIBAN";
        double withdrawalAmount = 50.0;

        Account account = new Account();
        account.setId(accountId);

        // Act
        accountService.withdraw(accountId, withdrawalAmount, "Test withdrawal");


        // Assert
        assertEquals(150.0, accountService.getAccountHistory(accountId).getBalance()); // Balance should be updated
    }

    @Test
    void testWithdrawThrowsBalanceNotSufficientException() {
        // Arrange
        String accountId = "NicolasIBAN";
        double withdrawalAmount = 250.0; // More than the balance

        Account account = new Account();
        account.setId(accountId);

        // Act & Assert
        BalanceNotSufficientException exception = assertThrows(
                BalanceNotSufficientException.class,
                () -> accountService.withdraw(accountId, withdrawalAmount, "Test withdrawal")
        );

        assertEquals("Balance not sufficient", exception.getMessage());
    }

    @Test
    void testDepositSuccessful() throws AccountNotFoundException, BalanceNotSufficientException {
        // Arrange
        String accountId = "NicolasIBAN";
        double depositAmount = 50.0;

        Account account = new Account();
        account.setId(accountId);

        // Act
        accountService.deposit(accountId, depositAmount, "Test deposit");

        // Assert
        assertEquals(250.0, accountService.getAccountHistory(accountId).getBalance()); // Balance should be updated
    }

    @Test
    void testDepositThrowsAccountNotFoundException() {
        // Arrange
        String accountId = "non-existent-account-id";
        double depositAmount = 50.0;

        // Act & Assert
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.deposit(accountId, depositAmount, "Test deposit")
        );

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void testGetAccountHistorySuccessful() throws AccountNotFoundException {

        String accountId = "NicolasIBAN";

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(100.0);

        List<AccountOperation> operations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AccountOperation operation = new AccountOperation();
            operation.setAmount(10.0 * (i + 1));
            operation.setOperationDate(new Date());
            operation.setType(OperationType.WITHDRAW);
            operation.setDescription("Operation " + (i + 1));
            operations.add(operation);
        }

        when(dtoMapper.fromAccountOperation(any())).thenReturn(new AccountOperationDTO());

        // Act
        AccountHistoryDTO historyDTO = accountService.getAccountHistory(accountId);

        // Assert
        assertNotNull(historyDTO);
        assertEquals(accountId, historyDTO.getAccountId());
        assertEquals(3, historyDTO.getAccountOperationDTOS().size());
    }

    @Test
    void testGetAccountHistoryThrowsAccountNotFoundException() {
        // Arrange
        String accountId = "non-existent-account-id";

        // Act & Assert
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccountHistory(accountId)
        );

        assertEquals("Account not Found", exception.getMessage());
    }

    private void initMockData() {
        // Create mock customers
        Customer customer = new Customer();
        customer.setName("Nicolas");
        customer.setEmail(customer.getName() + "@gmail.com");
        customers.add(customer);

        // Create mock accounts
        Account account = new Account();
        account.setId(customer.getName() + "IBAN");
        account.setBalance(200.0);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>());
        accounts.add(account);

        // Create mock account operations
        for (int i = 0; i < 3; i++) {
            AccountOperation operation = new AccountOperation();
            operation.setOperationDate(new Date());
            operation.setAmount(20.0);
            operation.setType(OperationType.DEPOSIT);
            operation.setAccount(account);
            account.getAccountOperations().add(operation);
            accountOperations.add(operation);
        }
        accountService.setAccounts(accounts);
        accountService.setCustomers(customers);
        accountService.setAccountOperations(accountOperations);
    }
}

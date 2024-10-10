package org.sid.sgbankbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sid.sgbankbackend.dto.AccountHistoryDTO;
import org.sid.sgbankbackend.dto.AccountOperationDTO;
import org.sid.sgbankbackend.enums.OperationType;
import org.sid.sgbankbackend.exceptions.AccountNotFoundException;
import org.sid.sgbankbackend.exceptions.BalanceNotSufficientException;
import org.sid.sgbankbackend.mapper.AccountMapperImpl;
import org.sid.sgbankbackend.model.Account;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.model.Customer;
import org.sid.sgbankbackend.repositories.AccountOperationRepository;
import org.sid.sgbankbackend.repositories.AccountRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountOperationRepository accountOperationRepository;

    @Mock
    private AccountMapperImpl dtoMapper;

    private Account mockAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAccount = createMockAccount();
    }

    @Test
    void testWithdrawSuccessful() throws AccountNotFoundException, BalanceNotSufficientException {
        // Arrange
        String accountId = mockAccount.getId();
        double withdrawalAmount = 50.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        accountService.withdraw(accountId, withdrawalAmount, "Test withdrawal");

        // Assert
        verify(accountRepository, times(1)).save(mockAccount);
        assertEquals(150.0, mockAccount.getBalance());
    }

    @Test
    void testWithdrawThrowsBalanceNotSufficientException() {
        // Arrange
        String accountId = mockAccount.getId();
        double withdrawalAmount = 250.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act & Assert
        BalanceNotSufficientException exception = assertThrows(
                BalanceNotSufficientException.class,
                () -> accountService.withdraw(accountId, withdrawalAmount, "Test withdrawal")
        );

        assertEquals("Balance not sufficient", exception.getMessage());
        verify(accountRepository, never()).save(mockAccount);
    }

    @Test
    void testDepositSuccessful() throws AccountNotFoundException {
        // Arrange
        String accountId = mockAccount.getId();
        double depositAmount = 50.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        accountService.deposit(accountId, depositAmount, "Test deposit");

        // Assert
        verify(accountRepository, times(1)).save(mockAccount);
        assertEquals(250.0, mockAccount.getBalance());
    }

    @Test
    void testDepositThrowsAccountNotFoundException() {
        // Arrange
        String accountId = "non-existent-account-id";
        double depositAmount = 50.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.deposit(accountId, depositAmount, "Test deposit")
        );

        assertEquals("BankAccount not found", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testGetAccountHistorySuccessful() throws AccountNotFoundException {
        // Arrange
        String accountId = mockAccount.getId();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        when(dtoMapper.fromAccountOperation(any(AccountOperation.class)))
                .thenReturn(new AccountOperationDTO());

        // Act
        AccountHistoryDTO historyDTO = accountService.getAccountHistory(accountId);

        // Assert
        assertNotNull(historyDTO);
        assertEquals(accountId, historyDTO.getAccountId());
        assertEquals(mockAccount.getAccountOperations().size(), historyDTO.getAccountOperationDTOS().size());
    }

    @Test
    void testGetAccountHistoryThrowsAccountNotFoundException() {
        // Arrange
        String accountId = "non-existent-account-id";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccountHistory(accountId)
        );

        assertEquals("Account not Found", exception.getMessage());
    }

    private Account createMockAccount() {
        Customer customer = new Customer();
        customer.setName("Nicolas");
        customer.setEmail("nicolas@gmail.com");

        Account account = new Account();
        account.setId(customer.getName() + "IBAN");
        account.setBalance(200.0);
        account.setCustomer(customer);

        List<AccountOperation> operations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AccountOperation operation = new AccountOperation();
            operation.setType(OperationType.DEPOSIT);
            operation.setAmount(50.0);
            operation.setOperationDate(new Date());
            operation.setAccount(account);
            operations.add(operation);
        }
        account.setAccountOperations(operations);

        return account;
    }
}

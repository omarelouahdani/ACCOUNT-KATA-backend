package org.sid.sgbankbackend.service;

import org.sid.sgbankbackend.dto.AccountHistoryDTO;
import org.sid.sgbankbackend.exceptions.BalanceNotSufficientException;
import org.sid.sgbankbackend.exceptions.AccountNotFoundException;

/**
 * Service interface for managing bank account operations.
 */
public interface AccountService {

    /**
     * Deposits a specified amount into the account identified by accountId.
     *
     * @param accountId   The ID of the account to deposit into.
     * @param amount      The amount to be deposited.
     * @param description A description of the deposit operation.
     * @throws AccountNotFoundException    If the account with the specified ID is not found.
     */
    void deposit(String accountId, double amount, String description) throws AccountNotFoundException;

    /**
     * Withdraws a specified amount from the account identified by accountId.
     *
     * @param accountId   The ID of the account to withdraw from.
     * @param amount      The amount to be withdrawn.
     * @param description A description of the withdrawal operation.
     * @throws BalanceNotSufficientException If the account balance is insufficient for the withdrawal.
     * @throws AccountNotFoundException      If the account with the specified ID is not found.
     */
    void withdraw(String accountId, double amount, String description) throws BalanceNotSufficientException, AccountNotFoundException;

    /**
     * Retrieves the account history for the account identified by accountId.
     *
     * @param accountId The ID of the account whose history is to be retrieved.
     * @return An AccountHistoryDTO containing the account's transaction history.
     * @throws AccountNotFoundException If the account with the specified ID is not found.
     */
    AccountHistoryDTO getAccountHistory(String accountId) throws AccountNotFoundException;

    /**
     * Initializes the data for accounts, customers, and operations.
     */
    void initData();

}

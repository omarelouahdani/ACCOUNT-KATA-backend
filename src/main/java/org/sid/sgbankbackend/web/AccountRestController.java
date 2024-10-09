package org.sid.sgbankbackend.web;

import org.sid.sgbankbackend.dto.AccountHistoryDTO;
import org.sid.sgbankbackend.dto.WithdrawDTO;
import org.sid.sgbankbackend.dto.DepositDTO;
import org.sid.sgbankbackend.exceptions.BalanceNotSufficientException;
import org.sid.sgbankbackend.exceptions.AccountNotFoundException;
import org.sid.sgbankbackend.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing account operations.
 */
@RestController
@CrossOrigin("*") // Allow cross-origin requests
@RequestMapping("/accounts") // Base URI for account-related endpoints
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Deposits an amount into an account.
     *
     * @param depositDTO The deposit data transfer object containing account ID, amount, and description.
     * @throws AccountNotFoundException If the account is not found.
     */
    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public void deposit(@RequestBody DepositDTO depositDTO) throws AccountNotFoundException {
        accountService.deposit(depositDTO.getAccountId(), depositDTO.getAmount(), depositDTO.getDescription());
    }

    /**
     * Withdraws an amount from an account.
     *
     * @param withdrawDTO The withdrawal data transfer object containing account ID, amount, and description.
     * @throws BalanceNotSufficientException If the account balance is insufficient.
     * @throws AccountNotFoundException      If the account is not found.
     */
    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public void withdraw(@RequestBody WithdrawDTO withdrawDTO)
            throws BalanceNotSufficientException, AccountNotFoundException {
        accountService.withdraw(withdrawDTO.getAccountId(), withdrawDTO.getAmount(), withdrawDTO.getDescription());
    }

    /**
     * Retrieves the transaction history for a specific account.
     *
     * @param accountId The ID of the account.
     * @return The account history data transfer object.
     * @throws AccountNotFoundException If the account is not found.
     */
    @GetMapping("/{accountId}/history/operations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId) throws AccountNotFoundException {
        return accountService.getAccountHistory(accountId);
    }
}
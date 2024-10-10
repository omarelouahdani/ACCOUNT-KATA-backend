package org.sid.sgbankbackend;


import org.sid.sgbankbackend.initialization.MemoryDataInitializer;
import org.sid.sgbankbackend.model.AccountOperation;
import org.sid.sgbankbackend.repositories.AccountOperationRepository;
import org.sid.sgbankbackend.repositories.AccountRepository;
import org.sid.sgbankbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class SgBankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgBankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            AccountRepository accountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            MemoryDataInitializer initializer = new MemoryDataInitializer();
            // Initialize customers and accounts
            var customers = initializer.initializeCustomers();
            var accounts = initializer.initializeAccounts(customers);

            // Save customers to the database
            customerRepository.saveAll(customers);

            // Save accounts and account operations to the database
            accounts.forEach(account -> {
                accountRepository.save(account);
                // Initialize and save account operations
                List<AccountOperation> accountOperations = new ArrayList<>();
                initializer.initializeAccountOperations(account, accountOperations);
                accountOperationRepository.saveAll(accountOperations);

            });
        };
    }

}

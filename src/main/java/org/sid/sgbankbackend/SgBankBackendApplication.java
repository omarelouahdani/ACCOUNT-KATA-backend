package org.sid.sgbankbackend;


import org.sid.sgbankbackend.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SgBankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgBankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            accountService.initData();
        };
    }

}

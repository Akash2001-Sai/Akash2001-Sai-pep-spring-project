package com.example;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String[] args) throws Exception {
        if (!accountRepository.existsById(9999)) {
            Account account = new Account();
            account.setAccountId(9999);
            account.setUsername("testUser9999");
            account.setPassword("TestPass123");

            accountRepository.save(account);
            System.out.println("Account with ID 9999 created");
        } else {
            System.out.println("Account with ID 9999 already exists.");
        }
    }
}

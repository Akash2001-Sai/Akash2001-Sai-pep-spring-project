package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Registers a new account if the username is unique and password is valid.
     * @param account the account to register
     * @return the saved Account with accountId
     */

     public Account register(Account account) {
        // validate the username
        if (account.getUsername () == null || account.getUsername().isBlank()){
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (account.getPassword() == null || account.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());

        // save the account
        return accountRepository.save(account);
     }

     /**
      * Logs in a user by validationg the username and password
      * @param username the account username
      * @param password the account password
      * @return the account if login is successful
      */

      public Account login(String username, String password) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        if (optionalAccount.isEmpty() || !optionalAccount.get().getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid Password Please Enter Correct Password");
        }

        return optionalAccount.get();
      }

      /**
       * Retrieves an account by ID
       * @param accountId the account ID
       * @return an optional containing the account or empty if not found
       */

       public Optional<Account> getAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
       }
}

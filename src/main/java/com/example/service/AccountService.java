package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DataConflictException;
import com.example.exception.InvalidInputException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Process new User registrations.
     * The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, 
     * and an Account with that username does not already exist. The new account will be saved to the database.
     * 
     * @param account contains username and password, but not account_id
     * @return Account contains the account_id of the new database record.
     * @throws DataConflictException if the username or account_id already exists
     * @throws InvalidInputException if the username or password do not meed requirements.
     */
    public Account register(Account account) throws InvalidInputException, DataConflictException {
        // validate input
        if(account == null || account.getUsername() == null || account.getPassword() == null
            || account.getUsername().length() < 1 || account.getUsername().length() > 255
            || account.getPassword().length() < 4 || account.getPassword().length() > 255)
        {
            throw new InvalidInputException();
        }
       // check for data conflict 
       Account test = accountRepository.findByUsername(account.getUsername());
       if(test != null) {
        throw new DataConflictException();
       }
        //Account newAccount = accountRepository.createAccount(account.getUsername(), account.getPassword());
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    /**
     * ## 2: Our API should be able to process User logins.
     * 
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
     * The request body will contain a JSON representation of an Account.

    - The login will be successful if and only if the username and password provided in the request body JSON 
    match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
    - If the login is not successful, the response status should be 401. (Unauthorized)
    */
    public Account login(Account account) {
        //TODO
        Account newAccount = accountRepository.findByUsername(account.getUsername());
        if(newAccount != null && newAccount.getPassword() != null 
            && newAccount.getPassword().equals(account.getPassword())) {
            return newAccount;
        }
        else {
            throw new InvalidInputException();
        }
    }
}

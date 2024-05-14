package com.example.accounts.service;

import com.example.accounts.dto.CustomerDto;
import com.example.accounts.entity.Accounts;

public interface IAccountsService {

    /**
     *
     * @param customerDto
     */
    Accounts createAccount(CustomerDto customerDto);

    /**
     *
     * @param email
     * @return a Customer's details
     */
    CustomerDto getAccount(String email);
}

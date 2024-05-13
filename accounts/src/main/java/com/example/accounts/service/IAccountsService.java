package com.example.accounts.service;

import com.example.accounts.dto.CustomerDto;
import com.example.accounts.entity.Accounts;

public interface IAccountsService {

    /**
     *
     * @param customerDto
     */
    Accounts createAccount(CustomerDto customerDto);
}

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

    /**
     *
     * @param customerDto
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param email
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String email);
}

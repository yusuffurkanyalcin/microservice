package com.example.accounts.dto;

public record AccountsMessageDto(
        Long accountNumber,
        String name,
        String email,
        String mobileNumber
) {
}

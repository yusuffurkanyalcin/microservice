package com.example.message.functions;

import com.example.message.dto.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    // creates an API endpoint http://localhost:9010/email
    @Bean
    public Function<AccountsMsgDto, AccountsMsgDto> email() {
        return accountsMsgDto -> {
            log.info("Sending email with the details: " + accountsMsgDto.toString());
            return accountsMsgDto;
        };
    }

    // creates an API endpoint http://localhost:9010/sms
    @Bean
    public Function<AccountsMsgDto, Long> sms() {
        return accountsMsgDto -> {
            log.info("Sending SMS with the details: " + accountsMsgDto.toString());
            return accountsMsgDto.accountNumber();
        };
    }
}

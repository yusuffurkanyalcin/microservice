package com.example.accounts.controller;

import com.example.accounts.constants.AccountsConstants;
import com.example.accounts.dto.AccountsContactInfoDto;
import com.example.accounts.dto.CustomerDto;
import com.example.accounts.dto.ErrorResponseDto;
import com.example.accounts.dto.ResponseDto;
import com.example.accounts.entity.Accounts;
import com.example.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Tag(
        name = "CRUD REST APIs for Accounts in FurkanBank",
        description = "CRUD REST APIs in Furkan's Bank to CREATE, UPDATE, DELETE and FETCH account details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final IAccountsService service;
    private final AccountsContactInfoDto accountsContactInfoDto;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer & Account inside FurkanBank"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<Accounts> createAccount(@Valid
                                                      @RequestBody
                                                              CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAccount(customerDto));
    }

    @Operation(
            summary = "Fetch Account details REST API",
            description = "REST API to fetch Account & Customer details based on an email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                             @Email(message = "Email address should be a valid value")
                                                                     String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.fetchAccount(email));
    }

    @Operation(
            summary = "Update Account details REST API",
            description = "REST API to update Customer & Account details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
        }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = service.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
    }

    @Operation(
            summary = "Delete Account & Customer details REST API",
            description = "REST API to delete Customer & Account details based on an email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            )
        }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp = "(^|[0-9]{10})", message = "Email address should be a valid value")
                                                                        String email) {
        boolean isDeleted = service.deleteAccount(email);
        if(isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
    }


    @Retry(name = "getContactInfoRetry", fallbackMethod = "getContactInfoRetryFallback")
    @RateLimiter(name = "getContactInfoRateLimiter", fallbackMethod = "getContactInfoRateLimiterFallback")
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() throws TimeoutException{
        logger.debug("getContactInfo() method invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

    public ResponseEntity<AccountsContactInfoDto> getContactInfoRetryFallback(Throwable throwable) {
        logger.debug("getContactInfoRetryFallback() method invoked");

        HashMap<String, String> contactDetails = new HashMap<>();
        contactDetails.put("name","Furkan Yalcin - Developer");
        contactDetails.put("email", "furkanyalcin@furkanbank.com");

        List<String> onCallSupport = new ArrayList<>();
        onCallSupport.add("(555) 555-5555");
        onCallSupport.add("(666) 666-6666");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AccountsContactInfoDto("Welcome to FurkanBank accounts related docker APIs ", contactDetails, onCallSupport));
    }

    public ResponseEntity<AccountsContactInfoDto> getContactInfoRateLimiterFallback(Throwable throwable) {
        logger.debug("getContactInfoRateLimiterFallback() method invoked");

        HashMap<String, String> contactDetails = new HashMap<>();
        contactDetails.put("name","Furkan Yalcin - Developer");
        contactDetails.put("email", "furkanyalcin@furkanbank.com");

        List<String> onCallSupport = new ArrayList<>();
        onCallSupport.add("(000) 000-0000");
        onCallSupport.add("(111) 111-1111");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AccountsContactInfoDto("Welcome to FurkanBank accounts related docker APIs ", contactDetails, onCallSupport));
    }
}

package com.zerobase.userapi.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.userapi.domain.customer.ChangeBalanceForm;
import com.zerobase.userapi.domain.customer.CustomerDto;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.service.customer.CustomerBalanceService;
import com.zerobase.userapi.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.userapi.exception.ErrorCode.NOT_FOUND_USER;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

        Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return ResponseEntity.ok(CustomerDto.from(customer));
    }

    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                 @RequestBody ChangeBalanceForm form) {

        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

        return ResponseEntity.ok(customerBalanceService.changeBalance(userVo.getId(), form).getCurrentMoney());
    }
}

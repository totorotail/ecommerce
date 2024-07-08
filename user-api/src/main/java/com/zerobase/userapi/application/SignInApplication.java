package com.zerobase.userapi.application;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserType;
import com.zerobase.userapi.domain.SignInForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.service.customer.CustomerService;
import com.zerobase.userapi.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.userapi.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부 체크
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        // 2. 토큰을 발행
        // 3. 토큰을 response
        return jwtAuthenticationProvider
                .createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }

    public String sellerLoginToken(SignInForm form) {
        Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        return jwtAuthenticationProvider
                .createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
    }
}

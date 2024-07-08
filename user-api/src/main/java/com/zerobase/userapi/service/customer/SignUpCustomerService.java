package com.zerobase.userapi.service.customer;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.repository.CustomerRepository;
import com.zerobase.userapi.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.userapi.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email.toLowerCase())
                .isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Customer customer = customerRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (customer.isVerify()) {
            throw new CustomException(ALREADY_VERIFY);
        } else if (!customer.getVerificationCode().equals(code)) {
            throw new CustomException(WRONG_VERIFICATION);
        } else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(EXPIRE_CODE);
        }

        customer.setVerify(true);
    }

    @Transactional
    public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setVerificationCode(verificationCode);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

            return customer.getVerifyExpiredAt();
        }

        // 예외처리
        throw new CustomException(NOT_FOUND_USER);
    }
}

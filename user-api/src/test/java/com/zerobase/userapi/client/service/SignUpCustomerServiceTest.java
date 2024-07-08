package com.zerobase.userapi.client.service;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.service.customer.SignUpCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    SignUpCustomerService signUpCustomerService;

    @Test
    void signUp() {
        // given
        SignUpForm form = SignUpForm.builder()
                .name("name")
                .birth(LocalDate.now())
                .email("abc@gmail.com")
                .password("1")
                .phone("01000000000").build();

        // when
        Long id = signUpCustomerService.signUp(form).getId();

        // then
        assertNotNull(id);
        assertEquals(1, id);

    }

}
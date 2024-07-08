package com.zerobase.userapi.application;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.exception.ErrorCode;
import com.zerobase.userapi.service.customer.SignUpCustomerService;
import com.zerobase.userapi.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SellerService sellerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public void sellerVerify(String email, String code) {
        sellerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        } else {
            Customer c = signUpCustomerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@dannymytester.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(c.getEmail(), c.getName(), "customer", code))
                    .build();
            log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm).getBody());

            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    public String sellerSignUp(SignUpForm form) {
        if (sellerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        } else {
            Seller seller = sellerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@dannymytester.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(seller.getEmail(), seller.getName(), "seller", code))
                    .build();

            log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm).getBody());

            sellerService.changeSellerValidateEmail(seller.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String type, String code) {
        StringBuilder builder = new StringBuilder();

        return builder.append("Hello ")
                .append(name)
                .append("! Please Click Link for verification\n\n")
                .append("http://localhost:8080/signup/" + type + "/verify/?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}

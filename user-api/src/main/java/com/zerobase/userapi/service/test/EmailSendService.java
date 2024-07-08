package com.zerobase.userapi.service.test;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public String sendEmail() {
        SendMailForm form = SendMailForm.builder()
                .from("zerobase-test@email.com")
                .to("iseungsoo07@gmail.com")
                .subject("Test email from zerobase")
                .text("my text")
                .build();

        return mailgunClient.sendEmail(form).getBody();
    }
}

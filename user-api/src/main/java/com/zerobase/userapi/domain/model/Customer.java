package com.zerobase.userapi.domain.model;


import com.zerobase.userapi.domain.SignUpForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone;
    private LocalDate birth;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    @Column(columnDefinition = "int default 0")
    private Integer balance;

    public static Customer from(SignUpForm form) {
        return Customer.builder()
                .email(form.getEmail())
                .password(form.getPassword())
                .name(form.getName())
                .birth(form.getBirth())
                .phone(form.getPhone())
                .verify(false)
                .build();
    }
}

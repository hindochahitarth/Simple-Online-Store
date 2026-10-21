package org.example.simpleonlinestore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_store")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class OtpEntity {
    @Id
    private String email;
    private String otp;
    private LocalDateTime expiryTime;

}

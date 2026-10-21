package org.example.simpleonlinestore.repository;

import org.example.simpleonlinestore.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface OtpRepository extends JpaRepository<OtpEntity,String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpEntity o WHERE o.expiryTime < :now")
    void deleteExpiredOtps(LocalDateTime now);
}

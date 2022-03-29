package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.OtpEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findByUserId(Long userId);
}
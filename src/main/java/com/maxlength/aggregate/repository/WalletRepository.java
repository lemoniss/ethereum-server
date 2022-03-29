package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long>, JpaSpecificationExecutor<WalletEntity> {

}
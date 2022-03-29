package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.spec.enums.Yesno;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {

    Optional<AccountEntity> findByUsernameAndDelYn(String username, Yesno delYn);

    AccountEntity findByIdAndDelYn(Long id, Yesno delYn);

}
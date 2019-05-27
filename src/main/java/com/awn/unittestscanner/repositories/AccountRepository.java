package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.Account;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Override
    <S extends Account> S save(S s);

    @Override
    Optional<Account> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Override
    Account getOne(Long aLong);

//    @Query(value = "SELECT a.id_account, a.account_status, a.email, a.full_name, a.password, a.password_status FROM Account a WHERE a.email = :email AND a.password = :password AND a.account_status = 'valid'", nativeQuery = true)
//    Account findAccountParamsNative(@Param("email") String email, @Param("password") String password);

    Account findAccountByEmailAndPasswordAndAccountStatus(String email, String password, String accountStatus);

    Optional<Account> findAccountByEmailAndAccountStatus(String email, String accountStatus);

    Optional<Account> findAccountByEmailAndPasswordStatus(String email, String passwordStatus);

}

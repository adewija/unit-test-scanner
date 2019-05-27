package com.awn.unittestscanner.services;

import com.awn.unittestscanner.dtos.AccountDTO;
import com.awn.unittestscanner.entities.Account;
import com.awn.unittestscanner.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public AccountDTO saveAccount(AccountDTO accountDTO) {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmailAndAccountStatus(accountDTO.getEmail(), "valid");

        if (optionalAccount.isPresent()) {
            return null;
        } else {
            Account account = new Account();
            account.setFullName(accountDTO.getFullName());
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setPasswordStatus("used");
            account.setAccountStatus("invalid");
            Account accountSaved = accountRepository.save(account);

            AccountDTO accountDTOFromEntity = convertToAccountDTO(accountSaved);
            return accountDTOFromEntity;
        }
    }

    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account accountBefore = new Account();
        Optional<Account> optionalAccountBefore = accountRepository.findById(accountDTO.getIdAccount());
        if (optionalAccountBefore.isPresent()) {
            optionalAccountBefore.ifPresent(account -> {
                account.setAccountStatus("valid");
                accountBefore.setPassword(account.getPassword());
                accountBefore.setPasswordStatus(account.getPasswordStatus());
                accountRepository.save(account);
            });
        }

        Optional<Account> optionalAccount = accountRepository.findAccountByEmailAndAccountStatus(accountDTO.getEmail(), "valid");

        if (optionalAccount.isPresent()) {

            optionalAccountBefore.ifPresent(account -> {
                account.setAccountStatus("valid");
                accountRepository.save(account);
            });

            return null;
        } else {
            Account account = new Account();
            account.setIdAccount(accountDTO.getIdAccount());
            account.setFullName(accountDTO.getFullName());
            account.setEmail(accountDTO.getEmail());
            account.setAccountStatus("invalid");
            account.setPassword(accountBefore.getPassword());
            account.setPasswordStatus(accountBefore.getPasswordStatus());
            Account accountSaved = accountRepository.save(account);

            AccountDTO accountDTOFromEntity = convertToAccountDTO(accountSaved);
            return accountDTOFromEntity;
        }
    }

    public void validateAccount(long idAccount) {
        Optional<Account> accountFromDB = accountRepository.findById(idAccount);

        if (accountFromDB.isPresent()) {
            accountFromDB.ifPresent(account -> {
                account.setAccountStatus("valid");
                accountRepository.save(account);
            });
        }
    }

    public AccountDTO checkEmail(String email) {
        Optional<Account> accountFromDB = accountRepository.findAccountByEmailAndAccountStatus(email, "valid");

        if (accountFromDB.isPresent()) {
            final AccountDTO[] accountDTOFromEntity = new AccountDTO[1];
            accountFromDB.ifPresent(account -> {
                account.setPasswordStatus("changed");
                accountRepository.save(account);
                accountDTOFromEntity[0] = convertToAccountDTO(account);
            });
            return accountDTOFromEntity[0];
        } else {
            return null;
        }
    }

    public boolean updatePassword(String email, String newPassword) {
        Optional<Account> accountFromDB = accountRepository.findAccountByEmailAndPasswordStatus(email, "changed");

        if (accountFromDB.isPresent()) {
            accountFromDB.ifPresent(account -> {
                account.setPassword(newPassword);
                account.setPasswordStatus("used");
                accountRepository.save(account);
            });
            return true;
        } else {
            return false;
        }
    }

    public boolean isAlreadyLoggedIn(HttpSession httpSession) {
        if (httpSession.getAttribute("fullName") != null) {
            return true;
        } else {
            return false;
        }
    }

    public AccountDTO findAccount(AccountDTO accountDTO) {
        Account account = accountRepository.findAccountByEmailAndPasswordAndAccountStatus(accountDTO.getEmail(), accountDTO.getPassword(), "valid");
        AccountDTO accountDTOFromEntity;
        if (account != null) {
            accountDTOFromEntity = convertToAccountDTO(account);
        } else {
            accountDTOFromEntity = null;
        }
        return accountDTOFromEntity;
    }

    public AccountDTO findAccountById(long id) {
        Account account = accountRepository.getOne(id);
        return convertToAccountDTO(account);
    }

    private AccountDTO convertToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount(account.getIdAccount());
        accountDTO.setAccountStatus(account.getAccountStatus());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setFullName(account.getFullName());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setPasswordStatus(account.getPasswordStatus());
        return accountDTO;
    }

    public void deleteAccountBeacuseEmailError(long id) {
        accountRepository.deleteById(id);
    }

    public void updateProfileBecauseEmailError(HttpSession httpSession, AccountDTO accountDTOAfter) {
        Optional<Account> optionalAccount = accountRepository.findById(accountDTOAfter.getIdAccount());
        if (optionalAccount.isPresent()) {
            optionalAccount.ifPresent(account -> {
                account.setFullName(httpSession.getAttribute("fullName").toString());
                account.setEmail(httpSession.getAttribute("email").toString());
                account.setAccountStatus("valid");
                accountRepository.save(account);
            });
        }

    }

    public void updatePasswordStatusBecauseEmailError(AccountDTO accountDTO) {
        Optional<Account> optionalAccountBefore = accountRepository.findById(accountDTO.getIdAccount());
        if (optionalAccountBefore.isPresent()) {
            optionalAccountBefore.ifPresent(account -> {
                account.setPasswordStatus("used");
                accountRepository.save(account);
            });
        }

    }


}

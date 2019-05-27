package com.awn.unittestscanner.dtos;

public class AccountDTO {

    private long idAccount;

    private String fullName;

    private String email;

    private String password;

    private String passwordStatus;

    private String accountStatus;

    public AccountDTO() {
    }

    public AccountDTO(long idAccount, String fullName, String email, String password, String passwordStatus, String accountStatus) {
        this.idAccount = idAccount;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.passwordStatus = passwordStatus;
        this.accountStatus = accountStatus;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordStatus() {
        return passwordStatus;
    }

    public void setPasswordStatus(String passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}

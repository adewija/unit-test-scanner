package com.awn.unittestscanner.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_account")
    private long idAccount;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "password_status")
    private String passwordStatus;

    @Column(name = "account_status")
    private String accountStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Project> projectList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<RuleGroup> ruleGroupList;

    public Account() {
    }

    public Account(String fullName, String email, String password, String passwordStatus, String accountStatus, List<Project> projectList, List<RuleGroup> ruleGroupList) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.passwordStatus = passwordStatus;
        this.accountStatus = accountStatus;
        this.projectList = projectList;
        this.ruleGroupList = ruleGroupList;
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

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<RuleGroup> getRuleGroupList() {
        return ruleGroupList;
    }

    public void setRuleGroupList(List<RuleGroup> ruleGroupList) {
        this.ruleGroupList = ruleGroupList;
    }
}

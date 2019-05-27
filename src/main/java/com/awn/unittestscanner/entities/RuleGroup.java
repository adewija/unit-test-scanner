package com.awn.unittestscanner.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rule_group")
public class RuleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rule_group")
    private long idRuleGroup;

    @Column(name = "rule_group_name")
    private String ruleGroupName;

    @Column(name = "rule_group_description")
    private String ruleGroupDescription;

    @OneToMany(mappedBy = "ruleGroup")
    private List<Rule> ruleList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ruleGroup")
    private List<Project> projectList;

    @ManyToOne
    private Account account;

    public RuleGroup() {
    }

    public RuleGroup(String ruleGroupName, String ruleGroupDescription, List<Rule> ruleList, List<Project> projectList, Account account) {
        this.ruleGroupName = ruleGroupName;
        this.ruleGroupDescription = ruleGroupDescription;
        this.ruleList = ruleList;
        this.projectList = projectList;
        this.account = account;
    }

    public long getIdRuleGroup() {
        return idRuleGroup;
    }

    public void setIdRuleGroup(long idRuleGroup) {
        this.idRuleGroup = idRuleGroup;
    }

    public String getRuleGroupName() {
        return ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }

    public String getRuleGroupDescription() {
        return ruleGroupDescription;
    }

    public void setRuleGroupDescription(String ruleGroupDescription) {
        this.ruleGroupDescription = ruleGroupDescription;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

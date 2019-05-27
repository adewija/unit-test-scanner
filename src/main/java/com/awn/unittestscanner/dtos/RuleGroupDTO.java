package com.awn.unittestscanner.dtos;


import java.util.List;

public class RuleGroupDTO {

    private long idRuleGroup;

    private String ruleGroupName;

    private String ruleGroupDescription;

    private List<RuleDTO> ruleList;

    private List<ProjectDTO> projectList;

    private AccountDTO account;


    public RuleGroupDTO() {
    }

    public RuleGroupDTO(long idRuleGroup, String ruleGroupName, String ruleGroupDescription, List<RuleDTO> ruleList, List<ProjectDTO> projectList, AccountDTO account) {
        this.idRuleGroup = idRuleGroup;
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

    public List<RuleDTO> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<RuleDTO> ruleList) {
        this.ruleList = ruleList;
    }

    public List<ProjectDTO> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}

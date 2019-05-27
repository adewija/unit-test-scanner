package com.awn.unittestscanner.dtos;

public class RuleDTO {

    private long idRule;

    private String ruleName;

    private String ruleSyntax;

    private String ruleDescription;

    private RuleGroupDTO ruleGroup;

    public RuleDTO() {
    }

    public RuleDTO(long idRule, String ruleName, String ruleSyntax, String ruleDescription, RuleGroupDTO ruleGroup) {
        this.idRule = idRule;
        this.ruleName = ruleName;
        this.ruleSyntax = ruleSyntax;
        this.ruleDescription = ruleDescription;
        this.ruleGroup = ruleGroup;
    }

    public long getIdRule() {
        return idRule;
    }

    public void setIdRule(long idRule) {
        this.idRule = idRule;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleSyntax() {
        return ruleSyntax;
    }

    public void setRuleSyntax(String ruleSyntax) {
        this.ruleSyntax = ruleSyntax;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public RuleGroupDTO getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(RuleGroupDTO ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}

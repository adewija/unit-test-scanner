package com.awn.unittestscanner.entities;

import javax.persistence.*;

@Entity
@Table(name = "rule")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rule")
    private long idRule;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "rule_syntax")
    private String ruleSyntax;

    @Column(name = "rule_description")
    private String ruleDescription;

    @ManyToOne
    private RuleGroup ruleGroup;

    public Rule() {
    }

    public Rule(String ruleName, String ruleSyntax, String ruleDescription, RuleGroup ruleGroup) {
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

    public RuleGroup getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(RuleGroup ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}

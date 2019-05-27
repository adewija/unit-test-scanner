package com.awn.unittestscanner.dtos;

import com.awn.unittestscanner.entities.RuleGroup;

import java.sql.Timestamp;
import java.util.List;

public class ProjectDTO {

    private long idProject;

    private String groupId;

    private String artifactId;

    private String version;

    private int numberUTClass;

    private Timestamp scanTime;

    private String projectPath;

    private List<WrongTestClassDTO> wrongTestClassList;

    private AccountDTO account;

    private RuleGroupDTO ruleGroup;

    public ProjectDTO() {
    }

    public long getIdProject() {
        return idProject;
    }

    public void setIdProject(long idProject) {
        this.idProject = idProject;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getNumberUTClass() {
        return numberUTClass;
    }

    public void setNumberUTClass(int numberUTClass) {
        this.numberUTClass = numberUTClass;
    }

    public Timestamp getScanTime() {
        return scanTime;
    }

    public void setScanTime(Timestamp scanTime) {
        this.scanTime = scanTime;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public List<WrongTestClassDTO> getWrongTestClassList() {
        return wrongTestClassList;
    }

    public void setWrongTestClassList(List<WrongTestClassDTO> wrongTestClassList) {
        this.wrongTestClassList = wrongTestClassList;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    private List<String> utClassNameList;

    public List<String> getUtClassNameList() {
        return utClassNameList;
    }

    public void setUtClassNameList(List<String> utClassNameList) {
        this.utClassNameList = utClassNameList;
    }

    public RuleGroupDTO getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(RuleGroupDTO ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}

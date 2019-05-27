package com.awn.unittestscanner.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_project")
    private long idProject;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "artifact_id")
    private String artifactId;

    @Column(name = "version")
    private String version;

    @Column(name = "number_ut_class")
    private int numberUTClass;

    @Column(name = "scan_time")
    private Timestamp scanTime;

    @Column(name = "project_path")
    private String projectPath;

    @OneToMany(mappedBy = "project")
    private List<WrongTestClass> wrongTestClassList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<WrongTestCode> wrongTestCodeList;

    @ManyToOne
    private Account account;

    @ManyToOne
    private RuleGroup ruleGroup;

    public Project() {
    }

    public Project(String groupId, String artifactId, String version, int numberUTClass, Timestamp scanTime, String projectPath, List<WrongTestClass> wrongTestClassList, List<WrongTestCode> wrongTestCodeList, Account account, RuleGroup ruleGroup) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.numberUTClass = numberUTClass;
        this.scanTime = scanTime;
        this.projectPath = projectPath;
        this.wrongTestClassList = wrongTestClassList;
        this.wrongTestCodeList = wrongTestCodeList;
        this.account = account;
        this.ruleGroup = ruleGroup;
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

    public List<WrongTestClass> getWrongTestClassList() {
        return wrongTestClassList;
    }

    public void setWrongTestClassList(List<WrongTestClass> wrongTestClassList) {
        this.wrongTestClassList = wrongTestClassList;
    }

    public List<WrongTestCode> getWrongTestCodeList() {
        return wrongTestCodeList;
    }

    public void setWrongTestCodeList(List<WrongTestCode> wrongTestCodeList) {
        this.wrongTestCodeList = wrongTestCodeList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public RuleGroup getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(RuleGroup ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}

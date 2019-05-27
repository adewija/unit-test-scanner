package com.awn.unittestscanner.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrong_test_class")
public class WrongTestClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_wrong_test_class")
    private long idWrongTestClass;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_path")
    private String classPath;

    @OneToMany(mappedBy = "wrongTestClass")
    private List<WrongTestCode> wrongTestCodeList;

    @ManyToOne
    private Project project;

    public WrongTestClass() {
    }

    public WrongTestClass(String className, String classPath, List<WrongTestCode> wrongTestCodeList, Project project) {
        this.className = className;
        this.classPath = classPath;
        this.wrongTestCodeList = wrongTestCodeList;
        this.project = project;
    }

    public long getIdWrongTestClass() {
        return idWrongTestClass;
    }

    public void setIdWrongTestClass(long idWrongTestClass) {
        this.idWrongTestClass = idWrongTestClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public List<WrongTestCode> getWrongTestCodeList() {
        return wrongTestCodeList;
    }

    public void setWrongTestCodeList(List<WrongTestCode> wrongTestCodeList) {
        this.wrongTestCodeList = wrongTestCodeList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

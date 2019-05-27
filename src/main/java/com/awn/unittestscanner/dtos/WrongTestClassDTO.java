package com.awn.unittestscanner.dtos;

import java.util.List;

public class WrongTestClassDTO {

    private long idWrongTestClass;

    private String className;

    private String classPath;

    private int numberOfWrongTestCode;

    private List<WrongTestCodeDTO> wrongTestCodeList;

    private ProjectDTO project;

    public WrongTestClassDTO() {
    }

    public WrongTestClassDTO(long idWrongTestClass, String className, String classPath, int numberOfWrongTestCode, List<WrongTestCodeDTO> wrongTestCodeList, ProjectDTO project) {
        this.idWrongTestClass = idWrongTestClass;
        this.className = className;
        this.classPath = classPath;
        this.numberOfWrongTestCode = numberOfWrongTestCode;
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

    public int getNumberOfWrongTestCode() {
        return numberOfWrongTestCode;
    }

    public void setNumberOfWrongTestCode(int numberOfWrongTestCode) {
        this.numberOfWrongTestCode = numberOfWrongTestCode;
    }

    public List<WrongTestCodeDTO> getWrongTestCodeList() {
        return wrongTestCodeList;
    }

    public void setWrongTestCodeList(List<WrongTestCodeDTO> wrongTestCodeList) {
        this.wrongTestCodeList = wrongTestCodeList;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }
}

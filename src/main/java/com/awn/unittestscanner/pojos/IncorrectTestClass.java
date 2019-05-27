package com.awn.unittestscanner.pojos;

import java.util.ArrayList;
import java.util.List;

public class IncorrectTestClass {

    private String className;

    private String classPath;

    private List<IncorrectTestCode> incorrectTestCodeList = new ArrayList<>();

    public IncorrectTestClass() {
    }

    public IncorrectTestClass(String className, String classPath, List<IncorrectTestCode> incorrectTestCodeList) {
        this.className = className;
        this.classPath = classPath;
        this.incorrectTestCodeList = incorrectTestCodeList;
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

    public List<IncorrectTestCode> getIncorrectTestCodeList() {
        return incorrectTestCodeList;
    }

    public void setIncorrectTestCodeList(List<IncorrectTestCode> incorrectTestCodeList) {
        this.incorrectTestCodeList = incorrectTestCodeList;
    }
}

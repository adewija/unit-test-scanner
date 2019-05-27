package com.awn.unittestscanner.entities;

import javax.persistence.*;

@Entity
@Table(name = "wrong_test_code")
public class WrongTestCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_wrong_test_code")
    private long idWrongTestCode;

    @Lob
    @Column(name = "code")
    private String code;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "line_number")
    private String lineNumber;

    @ManyToOne
    private WrongTestClass wrongTestClass;

    @ManyToOne
    private Project project;

    public WrongTestCode() {
    }

    public WrongTestCode(String code, String errorMessage, String lineNumber, WrongTestClass wrongTestClass, Project project) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
        this.wrongTestClass = wrongTestClass;
        this.project = project;
    }

    public long getIdWrongTestCode() {
        return idWrongTestCode;
    }

    public void setIdWrongTestCode(long idWrongTestCode) {
        this.idWrongTestCode = idWrongTestCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public WrongTestClass getWrongTestClass() {
        return wrongTestClass;
    }

    public void setWrongTestClass(WrongTestClass wrongTestClass) {
        this.wrongTestClass = wrongTestClass;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

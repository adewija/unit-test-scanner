package com.awn.unittestscanner.pojos;

public class IncorrectTestCode {

    private String code;

    private String name;

    private String parameter;

    private String errorMessage;

    private String lineNumber;

    public IncorrectTestCode() {
    }

    public IncorrectTestCode(String code, String name, String parameter, String errorMessage, String lineNumber) {
        this.code = code;
        this.name = name;
        this.parameter = parameter;
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
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
}

package com.awn.unittestscanner.dtos;

public class WrongTestCodeDTO {

    private long idWrongTestCode;

    private String code;

    private String errorMessage;

    private String lineNumber;

    private WrongTestClassDTO wrongTestClass;

    public WrongTestCodeDTO() {
    }

    public WrongTestCodeDTO(long idWrongTestCode, String code, String errorMessage, String lineNumber, WrongTestClassDTO wrongTestClass) {
        this.idWrongTestCode = idWrongTestCode;
        this.code = code;
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
        this.wrongTestClass = wrongTestClass;
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

    public WrongTestClassDTO getWrongTestClass() {
        return wrongTestClass;
    }

    public void setWrongTestClass(WrongTestClassDTO wrongTestClass) {
        this.wrongTestClass = wrongTestClass;
    }
}

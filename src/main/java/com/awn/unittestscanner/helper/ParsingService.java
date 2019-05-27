package com.awn.unittestscanner.helper;

import com.awn.unittestscanner.pojos.Enumeration;
import com.awn.unittestscanner.pojos.ClassReference;
import com.awn.unittestscanner.pojos.IncorrectTestClass;
import com.awn.unittestscanner.pojos.IncorrectTestCode;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParsingService implements Runnable {

    File testFile;
    List<String> ruleSyntaxList;
    List<ClassReference> classReferenceList;
    IncorrectTestClass incorrectTestClass;

    private List<MethodCallExpr> methodCallExprGlobal = new ArrayList<>();
    private List<MethodDeclaration> methodDeclarationGlobal = new ArrayList<>();
    private List<MarkerAnnotationExpr> markerAnnotationExprGlobal = new ArrayList<>();

    public ParsingService(File testFile, List<String> ruleSyntaxList, List<ClassReference> classReferenceList) {
        this.testFile = testFile;
        this.ruleSyntaxList = ruleSyntaxList;
        this.classReferenceList = classReferenceList;
    }

    public IncorrectTestClass getIncorrectTestClass() {
        return incorrectTestClass;
    }

    @Override
    public void run() {
        incorrectTestClass = new IncorrectTestClass();
        incorrectTestClass.setClassName(testFile.getName());
        incorrectTestClass.setClassPath(testFile.getPath());
        incorrectTestClass.setIncorrectTestCodeList(scanTestCodeByRules(testFile, ruleSyntaxList));
    }

    public List<IncorrectTestCode> scanTestCodeByRules(File testFile, List<String> ruleList) {
        List<IncorrectTestCode> incorrectTestCodeList = new ArrayList<>();


        for (String rule : ruleList) {

            String firstScanningType = rule.substring(0, rule.indexOf("|")).trim();
            String firstScanningParameter = rule.substring(rule.indexOf("|") + 1, rule.indexOf("->")).trim();
            String secondScanningType = rule.substring(rule.indexOf("->") + 2, rule.lastIndexOf("|")).trim();
            String secondScanningParameter = rule.substring(rule.lastIndexOf("|") + 1, rule.lastIndexOf("->")).trim();
            String errorMessage = rule.substring(rule.lastIndexOf("->") + 2).trim();

//============================================== Field Annotation ==============================================

            if (firstScanningType.equals(Enumeration.FIELD_ANNOTATION.getType())) {

                List<IncorrectTestCode> temporaryList = parseFieldAnnotation(firstScanningParameter, errorMessage);

                if (secondScanningType.equals(Enumeration.METHOD_CALL.getType())) {

                    scanSecondScanningTypeMethodCall(testFile, secondScanningParameter, temporaryList);

                }

//                else if (secondScanningType.equals(Enumeration.PARAMETER.getType())) {
//
//                    scanSecondScanningTypeParameter(secondScanningParameter, temporaryList);
//
//                }

                incorrectTestCodeList.addAll(temporaryList);

                System.out.println();

            }

//============================================== Method Call ==============================================

            else if (firstScanningType.equals(Enumeration.METHOD_CALL.getType())) {

                List<IncorrectTestCode> temporaryList = parseMethodCall(firstScanningParameter, errorMessage);

                if (secondScanningType.equals(Enumeration.METHOD_CALL.getType())) {

                    scanSecondScanningTypeMethodCall(testFile, secondScanningParameter, temporaryList);

                } else if (secondScanningType.equals(Enumeration.PARAMETER.getType())) {

                    scanSecondScanningTypeParameter(secondScanningParameter, temporaryList);

                }

                incorrectTestCodeList.addAll(temporaryList);

            }

//============================================== Object Operation Reading Variable ==============================================

            else if (firstScanningType.equals(Enumeration.OBJECT_OPERATION_READING_VARIABLE.getType())) {

                List<IncorrectTestCode> temporaryList = parseObjectOperationReadingVariable(errorMessage);

                if (secondScanningType.equals(Enumeration.METHOD_CALL.getType())) {

                    scanSecondScanningTypeMethodCall(testFile, secondScanningParameter, temporaryList);

                }

//                else if (secondScanningType.equals(Enumeration.PARAMETER.getType())) {
//
//                    scanSecondScanningTypeParameter(secondScanningParameter, temporaryList);
//
//                }

                incorrectTestCodeList.addAll(temporaryList);

            }
        }

        return incorrectTestCodeList;
    }



//    ========================================== Parse Field Anntotation ==================================================

    private List<IncorrectTestCode> parseFieldAnnotation(String firstScanningParameter, String errorMessage) {
        List<IncorrectTestCode> temporaryList = new ArrayList<>();

        if (markerAnnotationExprGlobal.isEmpty()) {

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MarkerAnnotationExpr markerAnnotationExpr, Object arg) {
                        super.visit(markerAnnotationExpr, arg);
                        markerAnnotationExprGlobal.add(markerAnnotationExpr);

                        if (markerAnnotationExpr.getNameAsString().equalsIgnoreCase(firstScanningParameter)) {

                            temporaryList.add(setIncorrectTestCodeForVariableAnnotation(markerAnnotationExpr, errorMessage));

                        }

                    }
                }.visit(JavaParser.parse(testFile), null);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            for (MarkerAnnotationExpr markerAnnotationExpr : markerAnnotationExprGlobal) {

                if (markerAnnotationExpr.getNameAsString().equalsIgnoreCase(firstScanningParameter)) {

                    temporaryList.add(setIncorrectTestCodeForVariableAnnotation(markerAnnotationExpr, errorMessage));

                }
            }
        }

        return temporaryList;
    }

    private IncorrectTestCode setIncorrectTestCodeForVariableAnnotation(MarkerAnnotationExpr markerAnnotationExpr, String errorMessage) {
        String parentNode = markerAnnotationExpr.getParentNode().get().toString().replaceAll("\\s+|\\r\\n", " ");
        String[] parentNodeSplitList = parentNode.split("\\s+|\\r\\n");
        String variableAnnotationName = parentNodeSplitList[parentNodeSplitList.length - 1].replaceAll(";", "");

        IncorrectTestCode incorrectTestCode = new IncorrectTestCode();
        incorrectTestCode.setCode(parentNode);
        incorrectTestCode.setName(variableAnnotationName);
        incorrectTestCode.setParameter("");
        incorrectTestCode.setLineNumber(findLineNumberInMAE(markerAnnotationExpr));
        incorrectTestCode.setErrorMessage(errorMessage.replace("%Code", incorrectTestCode.getCode()).replace("%LineNumber", incorrectTestCode.getLineNumber()));

        return incorrectTestCode;
    }

//    ========================================== Parse Method Call ==================================================

    private List<IncorrectTestCode> parseMethodCall(String firstScanningParameter, String errorMessage) {
        List<IncorrectTestCode> temporaryList = new ArrayList<>();

        if (methodCallExprGlobal.isEmpty()) {

            try {

                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr methodCallExpr, Object arg) {
                        super.visit(methodCallExpr, arg);
                        methodCallExprGlobal.add(methodCallExpr);

                        if (methodCallExpr.getNameAsString().equalsIgnoreCase(firstScanningParameter)) {

                            temporaryList.add(setIncorrectTestCodeForMethodCall(methodCallExpr, errorMessage));

                        }

                    }
                }.visit(JavaParser.parse(testFile), null);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            for (MethodCallExpr methodCallExpr : methodCallExprGlobal) {

                if (methodCallExpr.getNameAsString().equalsIgnoreCase(firstScanningParameter)) {

                    temporaryList.add(setIncorrectTestCodeForMethodCall(methodCallExpr, errorMessage));

                }

            }

        }

        return temporaryList;
    }

    private IncorrectTestCode setIncorrectTestCodeForMethodCall(MethodCallExpr methodCallExpr, String errorMessage) {

        String methodCallCode = methodCallExpr.getParentNode().get().getTokenRange().get().toString().replaceAll("\\s+|\\r\\n", "");
        String methodCallParameter = methodCallCode.substring(methodCallCode.indexOf("(") + 1, methodCallCode.lastIndexOf(")"));

        IncorrectTestCode incorrectTestCode = new IncorrectTestCode();
        incorrectTestCode.setCode(methodCallCode);
        incorrectTestCode.setName(methodCallExpr.getNameAsString());
        incorrectTestCode.setParameter(methodCallParameter);
        incorrectTestCode.setLineNumber(findLineNumberInMCE(methodCallExpr));
        incorrectTestCode.setErrorMessage(errorMessage.replace("%Code", incorrectTestCode.getCode()).replace("%LineNumber", incorrectTestCode.getLineNumber()));

        return incorrectTestCode;
    }

//    ========================================== Parse Object Operation Reading Variable ==================================================

    private List<IncorrectTestCode> parseObjectOperationReadingVariable(String errorMessage) {
        List<IncorrectTestCode> temporaryList = new ArrayList<>();

        if (methodDeclarationGlobal.isEmpty()) {

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration methodDeclaration, Object arg) {
                        super.visit(methodDeclaration, arg);
                        methodDeclarationGlobal.add(methodDeclaration);

                        setIncorrectTestCodeForObjectOperationReadingVariable(methodDeclaration, temporaryList, errorMessage);

                    }
                }.visit(JavaParser.parse(testFile), null);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            for (MethodDeclaration methodDeclaration : methodDeclarationGlobal) {

                setIncorrectTestCodeForObjectOperationReadingVariable(methodDeclaration, temporaryList, errorMessage);

            }

        }

        return temporaryList;
    }

    private void setIncorrectTestCodeForObjectOperationReadingVariable(MethodDeclaration methodDeclaration, List<IncorrectTestCode> temporaryList, String errorMessage) {

        int lineMethodBegin = methodDeclaration.getBegin().get().line;
        int lineMethodEnd = methodDeclaration.getEnd().get().line;

        if (methodDeclaration.getAnnotations().toString().contains("@Test")) {

            List<MethodCallExpr> methodCallExprList = methodDeclaration.findAll(MethodCallExpr.class);

            for (MethodCallExpr methodCallExpr : methodCallExprList) {

                if (methodCallExpr.getParentNode().get().getTokenRange().get().toString().contains("=")
                        && isVariable(findArguments(methodCallExpr.getParentNode().get().getTokenRange().get().toString()))
                        && lineMethodBegin < methodCallExpr.getBegin().get().line
                        && methodCallExpr.getBegin().get().line < lineMethodEnd) {

                    final boolean[] methodMainFound = {false};

                    classReferenceList.forEach(classReference -> classReference.getMethodWithReturnTypeList().forEach(methodWithReturnType -> {
                        if (methodWithReturnType.equals(methodCallExpr.getNameAsString())) {
                            methodMainFound[0] = true;
                        }
                    }));

                    if (methodMainFound[0]) {

                        IncorrectTestCode incorrectTestCode = new IncorrectTestCode();
                        incorrectTestCode.setCode(methodCallExpr.getParentNode().get().getTokenRange().get().toString());
                        incorrectTestCode.setName(findVariabel(methodCallExpr));
                        incorrectTestCode.setParameter("");
                        incorrectTestCode.setLineNumber(findLineNumberInMCE(methodCallExpr) + "|" + lineMethodBegin + "-" + lineMethodEnd);
                        incorrectTestCode.setErrorMessage(errorMessage.replace("%Code", incorrectTestCode.getCode()).replace("%LineNumber", incorrectTestCode.getLineNumber().substring(0, incorrectTestCode.getLineNumber().indexOf("|"))));

                        temporaryList.add(incorrectTestCode);
                    }

                }
            }

        }

    }


//    ============================================================================================


    private void scanSecondScanningTypeMethodCall(File testFile, String secondScanningParameter, List<IncorrectTestCode> temporaryList) {

        if (methodCallExprGlobal.isEmpty()) {

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr methodCallExpr, Object arg) {
                        super.visit(methodCallExpr, arg);
                        methodCallExprGlobal.add(methodCallExpr);

                        makeSelectionBasedOnCondition(methodCallExpr, secondScanningParameter, temporaryList);

                    }
                }.visit(JavaParser.parse(testFile), null);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            for (MethodCallExpr methodCallExpr : methodCallExprGlobal) {

                makeSelectionBasedOnCondition(methodCallExpr, secondScanningParameter, temporaryList);

            }

        }

    }

    private void makeSelectionBasedOnCondition(MethodCallExpr methodCallExpr, String secondScanningParameter, List<IncorrectTestCode> temporaryList) {
        boolean isNotOnlyCondition = false;

        if (secondScanningParameter.contains(" " + Enumeration.NOT_ONLY.getType() + " ")) {
            isNotOnlyCondition = true;
        }

        if (secondScanningParameter.contains(Enumeration.EQUALS.getType() + " ")) {

            String secondScanningParameterReplacedEquals = secondScanningParameter.replace("Equals", "").trim();

            if (isNotOnlyCondition) {
                secondScanningParameterReplacedEquals = secondScanningParameterReplacedEquals.substring(0, secondScanningParameterReplacedEquals.indexOf("&")).trim();
            }

            if (methodCallExpr.getNameAsString().equals(secondScanningParameterReplacedEquals)) {
                makeSelectionInMethodCall(methodCallExpr, temporaryList, secondScanningParameter, isNotOnlyCondition);
            }

        } else if (secondScanningParameter.contains(Enumeration.CONTAINS.getType() + " ")) {

            String secondScanningParameterReplacedEquals = secondScanningParameter.replace("Contains", "").trim();

            if (isNotOnlyCondition) {
                secondScanningParameterReplacedEquals = secondScanningParameterReplacedEquals.substring(0, secondScanningParameterReplacedEquals.indexOf("&")).trim();
            }

            if (methodCallExpr.getNameAsString().contains(secondScanningParameterReplacedEquals)) {
                makeSelectionInMethodCall(methodCallExpr, temporaryList, secondScanningParameter, isNotOnlyCondition);
            }

        }
    }

    private void makeSelectionInMethodCall(MethodCallExpr methodCallExpr, List<IncorrectTestCode> temporaryList, String secondScanningParameter, boolean isNotOnlyCondition) {
        int lineMethodBegin;
        int lineMethodEnd;
        int numberOfLine;
        String notOnlyCondition;

        String methodCallCode = methodCallExpr.getParentNode().get().getTokenRange().get().toString().replaceAll("\\s+|\\r\\n", "");

        if (methodCallExpr.getName().asString().equals("verify")) {
            if (methodCallCode.substring(methodCallCode.indexOf("verify("), methodCallCode.indexOf(")")).contains(",")) {
                String times = methodCallCode.substring(methodCallCode.indexOf(","), methodCallCode.indexOf(")") + 1);
                methodCallCode = methodCallCode.replace(times, "");
            }
        }

        String methodCallParameter = methodCallCode.substring(methodCallCode.indexOf("(") + 1, methodCallCode.lastIndexOf(")")).replace(" ", "");
        String lineNumber = findLineNumberInMCE(methodCallExpr);

        List<IncorrectTestCode> temporaryListDeleted = new ArrayList<>();

        for (IncorrectTestCode preliminaryData : temporaryList) {

            if (isNotOnlyCondition) {

                notOnlyCondition = secondScanningParameter.substring(secondScanningParameter.indexOf("& NotOnly") + 10).trim();

                if (preliminaryData.getLineNumber().contains("|")) {

                    lineMethodBegin = Integer.parseInt(preliminaryData.getLineNumber().substring(preliminaryData.getLineNumber().indexOf("|") + 1, preliminaryData.getLineNumber().lastIndexOf("-")));
                    lineMethodEnd = Integer.parseInt(preliminaryData.getLineNumber().substring(preliminaryData.getLineNumber().lastIndexOf("-") + 1));

                    if (!lineNumber.contains("-")) {
                        numberOfLine = Integer.parseInt(lineNumber);
                    } else {
                        numberOfLine = Integer.parseInt(lineNumber.substring(0, lineNumber.indexOf("-")));
                    }

                    if (methodCallParameter.contains(preliminaryData.getName())
                            && lineMethodBegin < numberOfLine
                            && numberOfLine < lineMethodEnd
                            && !methodCallCode.contains(notOnlyCondition)) {
                        temporaryListDeleted.add(preliminaryData);
                    }

                } else if (preliminaryData.getParameter().contains("(") && preliminaryData.getParameter().contains(")")) {

                    temporaryListDeleted = cleanRoundBracketsAndMakeSelection(preliminaryData, methodCallParameter, temporaryListDeleted);

                } else {

                    if (methodCallParameter.contains(preliminaryData.getName()) && !methodCallCode.contains(notOnlyCondition)) {
                        temporaryListDeleted.add(preliminaryData);
                    }
                }

            } else {

                if (methodCallParameter.contains(preliminaryData.getName())) {

                    temporaryListDeleted.add(preliminaryData);

                } else if (preliminaryData.getParameter().contains("(") && preliminaryData.getParameter().contains(")")) {

                    temporaryListDeleted = cleanRoundBracketsAndMakeSelection(preliminaryData, methodCallParameter, temporaryListDeleted);

                }

            }
        }

        temporaryList.removeAll(temporaryListDeleted);
    }

    private List<IncorrectTestCode> cleanRoundBracketsAndMakeSelection(IncorrectTestCode preliminaryData, String methodCallParameter, List<IncorrectTestCode> temporaryListDeleted) {

        String preliminaryDataParameter = preliminaryData.getCode().replace("(", "").replace(")", "").replace(" ", "");

        String param = methodCallParameter.replace("(", "").replace(")", "");

        if (preliminaryDataParameter.contains(param)) {
            temporaryListDeleted.add(preliminaryData);
        }

        return temporaryListDeleted;
    }

//    ============================================================================================

    private void scanSecondScanningTypeParameter(String secondScanningParameter, List<IncorrectTestCode> temporaryList) {
        List<IncorrectTestCode> temporaryListDeleted = new ArrayList<>();

        for (IncorrectTestCode preliminaryData : temporaryList) {
            String preliminaryDataParameter = preliminaryData.getParameter().replace("(", "").replace(")", "").replace(" ", "");

            if (secondScanningParameter.contains(Enumeration.EXCEPT.getType() + " ")) {

                String exceptParameter = secondScanningParameter.replace("Except", "").replace(" ", "").trim();

                if (secondScanningParameter.contains(" " + Enumeration.EQUALS.getType() + " ")) {
                    String equalsParameter = exceptParameter.replace("Equals", "").replace(" ", "").trim();
                    if (!preliminaryDataParameter.equals(equalsParameter)) {
                        temporaryListDeleted.add(preliminaryData);
                    }
                } else if (secondScanningParameter.contains(" " + Enumeration.CONTAINS.getType() + " ")) {
                    String containsParameter = exceptParameter.replace("Contains", "").replace(" ", "").trim();
                    if (!preliminaryDataParameter.contains(containsParameter)) {
                        temporaryListDeleted.add(preliminaryData);
                    }
                }

            } else {

                if (secondScanningParameter.contains(Enumeration.EQUALS.getType() + " ")) {
                    String equalsParameter = secondScanningParameter.replace("Equals", "").replace(" ", "").trim();
                    if (preliminaryDataParameter.equals(equalsParameter)) {
                        temporaryListDeleted.add(preliminaryData);
                    }
                } else if (secondScanningParameter.contains(Enumeration.CONTAINS.getType() + " ")) {
                    String containsParameter = secondScanningParameter.replace("Contains", "").replace(" ", "").trim();
                    if (preliminaryDataParameter.contains(containsParameter)) {
                        temporaryListDeleted.add(preliminaryData);
                    }
                }

            }
        }

        temporaryList.removeAll(temporaryListDeleted);

    }


//============================================== Method Helper ==============================================

    private String findArguments(String argumentWithTokenRange) {
        String tempArgument = argumentWithTokenRange;
        tempArgument = tempArgument.replaceAll("\\s+", "");
        return tempArgument;
    }

    private boolean isVariable(String variable) {
        boolean result = false;
        if (variable.contains("=")) {
            char charAt = variable.charAt(variable.lastIndexOf("=") - 1);
            if (Character.isLetter(charAt)) {
                result = true;
            }
        }
        return result;
    }

    private String findLineNumberInMCE(MethodCallExpr methodCallExpr) {
        String lineNumber;
        if (methodCallExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line
                == methodCallExpr.getParentNode().get().getTokenRange().get().getEnd().getRange().get().end.line) {
            lineNumber = String.valueOf(methodCallExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line);
        } else {
            lineNumber = methodCallExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line +
                    "-" + methodCallExpr.getParentNode().get().getTokenRange().get().getEnd().getRange().get().end.line;
        }
        return lineNumber;
    }

    private String findLineNumberInMAE(MarkerAnnotationExpr markerAnnotationExpr) {
        String lineNumber;
        if (markerAnnotationExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line
                == markerAnnotationExpr.getParentNode().get().getTokenRange().get().getEnd().getRange().get().end.line) {
            lineNumber = String.valueOf(markerAnnotationExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line);
        } else {
            lineNumber = markerAnnotationExpr.getParentNode().get().getTokenRange().get().getBegin().getRange().get().begin.line +
                    "-" + markerAnnotationExpr.getParentNode().get().getTokenRange().get().getEnd().getRange().get().end.line;
        }
        return lineNumber;
    }

    private String findVariabel(MethodCallExpr methodCallExpr) {
        String origin = findArguments(methodCallExpr.getParentNode().get().getTokenRange().get().toString());

        if (origin.contains("this.")) {
            origin = origin.replaceAll("this.", "");
        }

        origin = origin.substring(0, origin.indexOf("="));
        String[] tempArray = origin.split("\\s+");
        origin = tempArray[tempArray.length - 1];
        return origin;
    }

}

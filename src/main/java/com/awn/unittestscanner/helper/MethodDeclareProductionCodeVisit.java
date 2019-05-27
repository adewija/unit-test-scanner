package com.awn.unittestscanner.helper;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodDeclareProductionCodeVisit extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(MethodDeclaration methodDeclaration, List<String> methodWithReturnTypeList) {
        if (!methodDeclaration.getType().isVoidType()){
            methodWithReturnTypeList.add(methodDeclaration.getNameAsString());
        }
        super.visit(methodDeclaration,  methodWithReturnTypeList);
    }

}
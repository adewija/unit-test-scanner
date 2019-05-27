package com.awn.unittestscanner.helper;

import com.awn.unittestscanner.pojos.ClassReference;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclareProductionCodeVisit extends VoidVisitorAdapter<ClassReference> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, ClassReference classReference) {
        List<String> methodWithReturnTypeList = new ArrayList<>();
        classReference.setClassName(classOrInterfaceDeclaration.getNameAsString());
        classOrInterfaceDeclaration.accept(new MethodDeclareProductionCodeVisit(), methodWithReturnTypeList);
        classReference.setMethodWithReturnTypeList(methodWithReturnTypeList);
        super.visit(classOrInterfaceDeclaration, classReference);
    }
}
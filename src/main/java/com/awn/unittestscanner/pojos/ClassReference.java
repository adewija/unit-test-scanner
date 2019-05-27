package com.awn.unittestscanner.pojos;

import java.util.ArrayList;
import java.util.List;

public class ClassReference {
  String className;
  List<String> methodWithReturnTypeList = new ArrayList<>();

  public ClassReference() {
  }

  public ClassReference(String className, List<String> methodWithReturnTypeList) {
    this.className = className;
    this.methodWithReturnTypeList = methodWithReturnTypeList;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public List<String> getMethodWithReturnTypeList() {
    return methodWithReturnTypeList;
  }

  public void setMethodWithReturnTypeList(List<String> methodWithReturnTypeList) {
    this.methodWithReturnTypeList = methodWithReturnTypeList;
  }
}

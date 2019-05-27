package com.awn.unittestscanner.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectoryService {

  List<File> foundTestFile = new ArrayList<>();

  List<File> foundMainFile = new ArrayList<>();

  private List<File> pomFiles = new ArrayList<>();

  String testClassPath;

  String scanResultFilePath;

  public List<File> findMainFile(File file) {
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::findMainFile);
    } else {
      if ((file.getPath().endsWith(".java") && file.getPath().contains("/src/") && !file.getPath().contains("/test/")) && !file.getPath()
              .endsWith("Test.java")) {
        foundMainFile.add(file);
      }
    }
    return foundMainFile;
  }

  public String findScanResultFilePath(File file) {
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::findScanResultFilePath);
    } else {
      if ((file.getPath().endsWith("ScanResult.xlsx"))) {
        scanResultFilePath = file.getPath();
        return scanResultFilePath;
      }
    }
    return scanResultFilePath;
  }

  public String findTestFilePath(File file, String classPathPartial) {
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(childFile -> {
        findTestFilePath(childFile, classPathPartial);
      } );
    } else {
      if ((file.getPath().contains("/src/") && file.getPath().contains("/test/")) && file.getPath()
              .contains(classPathPartial)) {
        testClassPath = file.getPath();
        return testClassPath;
      }
    }
    return testClassPath;
  }

  public List<File> findPomFiles(File file) {
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::findPomFiles);
    } else {
      if (file.getPath().endsWith("pom.xml")) {
        pomFiles.add(file);
      }
    }
    return pomFiles;
  }

  public List<File> findTestFiles(File file) {
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::findTestFiles);
    } else {
      if (file.getPath().contains("/src/test/") && file.getPath().endsWith("Test.java")) {
        foundTestFile.add(file);
      }
    }
    return foundTestFile;
  }

}
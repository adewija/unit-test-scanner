package com.awn.unittestscanner.helper;

import com.awn.unittestscanner.helper.ParsingService;
import com.awn.unittestscanner.pojos.ClassReference;
import com.awn.unittestscanner.pojos.IncorrectTestCode;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParsingServiceTest {

    @Test
    public void scanTestCodeByRulesPath1(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");
        List<String> ruleList = new ArrayList<>();

        List<ClassReference> classReferenceList = new ArrayList<>();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();
        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(),incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath2(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Field Annotation | Mock -> Scanning Type | Equals verifyNoMoreInteractions -> Couldn't Find Matching VerifyNoMoreInteractions Method for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        IncorrectTestCode incorrectTestCode = new IncorrectTestCode();
        incorrectTestCode.setName("projectRepository");

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();
        incorrectTestCodeListExpected.add(incorrectTestCode);

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(),incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath3() {
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Field Annotation | Mock -> Method Call | Equals verifyNoMoreInteractions -> Couldn't Find Matching VerifyNoMoreInteractions Method for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath4(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Method Call | when -> Method Call | Equals verify -> Couldn't Find Matching Verify Method for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath5(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Method Call | when -> Scannig Type | Equals verify -> Couldn't Find Matching Verify Method for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        IncorrectTestCode incorrectTestCode1 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode2 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode3 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode4 = new IncorrectTestCode();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();
        incorrectTestCodeListExpected.add(incorrectTestCode1);
        incorrectTestCodeListExpected.add(incorrectTestCode2);
        incorrectTestCodeListExpected.add(incorrectTestCode3);
        incorrectTestCodeListExpected.add(incorrectTestCode4);

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath6(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Method Call | assertTrue -> Parameter | Except Equals true -> Don't Use True as a Parameter for : %Code at Line %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath7(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Scanning Type | assertTrue -> Parameter | Except Equals true -> Don't Use True as a Parameter for : %Code at Line %LineNumber";
        ruleList.add(rule);

        List<ClassReference> classReferenceList = new ArrayList<>();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());

    }

    @Test
    public void scanTestCodeByRulesPath8(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Object Operation Reading Variable | All -> Scanning Type | Contains assert & NotOnly assertNotNull -> Couldn't Find Matching Method to be Asserted for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<String> methodReturnTypeList = new ArrayList<>();
        methodReturnTypeList.add("collectProjectInformation");
        methodReturnTypeList.add("checkMainPomFile");
        methodReturnTypeList.add("findScanResultFileCreated");

        ClassReference classReference = new ClassReference();
        classReference.setClassName("projectService");
        classReference.setMethodWithReturnTypeList(methodReturnTypeList);

        List<ClassReference> classReferenceList = new ArrayList<>();
        classReferenceList.add(classReference);

        IncorrectTestCode incorrectTestCode1 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode2 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode3 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode4 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode5 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode6 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode7 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode8 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode9 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode10 = new IncorrectTestCode();
        IncorrectTestCode incorrectTestCode11 = new IncorrectTestCode();

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();
        incorrectTestCodeListExpected.add(incorrectTestCode1);
        incorrectTestCodeListExpected.add(incorrectTestCode2);
        incorrectTestCodeListExpected.add(incorrectTestCode3);
        incorrectTestCodeListExpected.add(incorrectTestCode4);
        incorrectTestCodeListExpected.add(incorrectTestCode5);
        incorrectTestCodeListExpected.add(incorrectTestCode6);
        incorrectTestCodeListExpected.add(incorrectTestCode7);
        incorrectTestCodeListExpected.add(incorrectTestCode8);
        incorrectTestCodeListExpected.add(incorrectTestCode9);
        incorrectTestCodeListExpected.add(incorrectTestCode10);
        incorrectTestCodeListExpected.add(incorrectTestCode11);

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }

    @Test
    public void scanTestCodeByRulesPath9(){
        File file = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData5/DataTest.java");

        List<String> ruleList = new ArrayList<>();
        String rule = "Object Operation Reading Variable | All -> Method Call | Contains assert & NotOnly assertNotNull -> Couldn't Find Matching Method to be Asserted for : %Code at Line: %LineNumber";
        ruleList.add(rule);

        List<String> methodReturnTypeList = new ArrayList<>();
        methodReturnTypeList.add("collectProjectInformation");
        methodReturnTypeList.add("checkMainPomFile");
        methodReturnTypeList.add("findScanResultFileCreated");

        ClassReference classReference = new ClassReference();
        classReference.setClassName("projectService");
        classReference.setMethodWithReturnTypeList(methodReturnTypeList);

        List<ClassReference> classReferenceList = new ArrayList<>();
        classReferenceList.add(classReference);

        List<IncorrectTestCode> incorrectTestCodeListExpected = new ArrayList<>();

        ParsingService parsingService = new ParsingService(file, ruleList, classReferenceList);

        List<IncorrectTestCode> incorrectTestCodeListActual = parsingService.scanTestCodeByRules(file, ruleList);

        assertEquals(incorrectTestCodeListExpected.size(), incorrectTestCodeListActual.size());
    }
}

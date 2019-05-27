package com.awn.unittestscanner.services;

import com.awn.unittestscanner.dtos.ProjectDTO;
import com.awn.unittestscanner.entities.*;
import com.awn.unittestscanner.helper.ClassDeclareProductionCodeVisit;
import com.awn.unittestscanner.helper.DirectoryService;
import com.awn.unittestscanner.helper.ParsingService;
import com.awn.unittestscanner.pojos.ClassReference;
import com.awn.unittestscanner.pojos.IncorrectTestClass;
import com.awn.unittestscanner.repositories.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScanningService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    WrongTestClassRepository wrongTestClassRepository;

    @Autowired
    WrongTestCodeRepository wrongTestCodeRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    RuleGroupRepository ruleGroupRepository;


    public long scan(String ruleGroupName, HttpSession httpSession, File projectDirectory, ProjectDTO projectDTO) {

        DirectoryService directoryService = new DirectoryService();

        List<IncorrectTestClass> incorrectTestClassList = new ArrayList<>();

        Optional<RuleGroup> ruleGroupOptional = ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupName, (Long) httpSession.getAttribute("idAccount"));

        List<String> ruleSyntaxList = new ArrayList<>();

        RuleGroup ruleGroup = new RuleGroup();

        if (ruleGroupOptional.isPresent()) {

            ruleGroup = ruleGroupOptional.get();

            List<Rule> ruleList = ruleGroupOptional.get().getRuleList();
            for (Rule rule: ruleList) {
                ruleSyntaxList.add(rule.getRuleSyntax());
            }
        }

        Timestamp scanTime = new Timestamp(System.currentTimeMillis());

        projectDTO.setScanTime(scanTime);

        List<ClassReference> classReferenceList = scanProductionCode(projectDirectory);

        List<File> listOfTestFile = directoryService.findTestFiles(projectDirectory);

        for (File testFile : listOfTestFile) {

            ParsingService parsingService = new ParsingService(testFile, ruleSyntaxList, classReferenceList);
            Thread parseFile = new Thread(parsingService);
            parseFile.run();

            if (parsingService.getIncorrectTestClass().getIncorrectTestCodeList().size() > 0) {
                incorrectTestClassList.add(parsingService.getIncorrectTestClass());
            }

        }

        return saveResultToDB(incorrectTestClassList, projectDTO, httpSession, ruleGroup);
    }

    private List<ClassReference> scanProductionCode(File projectDirectory) {
        DirectoryService directoryService = new DirectoryService();
        List<ClassReference> classReferenceList = new ArrayList<>();

        List<File> mainFileList = directoryService.findMainFile(projectDirectory);

        for (File file : mainFileList) {
            try {
                ClassReference classReference = new ClassReference();
                CompilationUnit compilationUnit = JavaParser.parse(file);

                compilationUnit.accept(new ClassDeclareProductionCodeVisit(), classReference);

                classReferenceList.add(classReference);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classReferenceList;
    }

    private long saveResultToDB(List<IncorrectTestClass> incorrectTestClassList, ProjectDTO projectDTO, HttpSession httpSession, RuleGroup ruleGroup) {

        Account account = accountRepository.getOne((Long) httpSession.getAttribute("idAccount"));

        String previousProjectPath = findPreviousProjectAndGetPreviousProjectPath(projectDTO, account);

        Project project = new Project();

        project.setGroupId(projectDTO.getGroupId());
        project.setArtifactId(projectDTO.getArtifactId());
        project.setVersion(projectDTO.getVersion());
        project.setNumberUTClass(projectDTO.getNumberUTClass());
        project.setScanTime(projectDTO.getScanTime());
        project.setProjectPath(projectDTO.getProjectPath());
        project.setAccount(account);
        project.setRuleGroup(ruleGroup);

        Project projectSaved = projectRepository.save(project);

        incorrectTestClassList.forEach(incorrectTestClass -> {

            WrongTestClass wrongTestClass = new WrongTestClass();

            wrongTestClass.setClassName(incorrectTestClass.getClassName().replace(".java", ""));

            wrongTestClass.setClassPath(incorrectTestClass.getClassPath().substring(incorrectTestClass.getClassPath().indexOf("/src")));

            wrongTestClass.setProject(projectSaved);

            WrongTestClass wrongTestClassSaved = wrongTestClassRepository.save(wrongTestClass);

            if (incorrectTestClass.getIncorrectTestCodeList() != null) {

                incorrectTestClass.getIncorrectTestCodeList().forEach(incorrectTestCode -> {

                    WrongTestCode wrongTestCode = new WrongTestCode();

                    wrongTestCode.setCode(incorrectTestCode.getCode());

                    if (incorrectTestCode.getLineNumber().contains("|")){
                        wrongTestCode.setLineNumber(incorrectTestCode.getLineNumber().substring(0, incorrectTestCode.getLineNumber().indexOf("|")));
                    } else {
                        wrongTestCode.setLineNumber(incorrectTestCode.getLineNumber());
                    }

                    wrongTestCode.setErrorMessage(incorrectTestCode.getErrorMessage());

                    wrongTestCode.setWrongTestClass(wrongTestClassSaved);

                    wrongTestCode.setProject(projectSaved);

                    wrongTestCodeRepository.save(wrongTestCode);

                });
            }

        });

        if (previousProjectPath != null) {
            try {
                projectService.deleteFileOrDirectory(previousProjectPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        projectService.deleteDataInStaticObject();

        return projectSaved.getIdProject();
    }

    private String findPreviousProjectAndGetPreviousProjectPath(ProjectDTO projectDTO, Account account) {
        Optional<Project> projectOptional = projectRepository.findProjectByGroupIdAndArtifactIdAndAccount(projectDTO.getGroupId(), projectDTO.getArtifactId(), account);

        final String[] previousProjectPath = new String[1];

        if (projectOptional.isPresent()) {
            projectOptional.ifPresent(project -> {
                previousProjectPath[0] = project.getProjectPath();
                wrongTestCodeRepository.deleteWrongTestCodesByProject(project);
                wrongTestClassRepository.deleteWrongTestClassesByProject(project);
                projectRepository.delete(project);
            });
        }

        return previousProjectPath[0];
    }

}
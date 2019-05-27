package com.awn.unittestscanner.controllers;

import com.awn.unittestscanner.services.ScanningService;
import com.awn.unittestscanner.dtos.*;
import com.awn.unittestscanner.services.AccountService;
import com.awn.unittestscanner.helper.EmailService;
import com.awn.unittestscanner.services.ProjectService;
import com.awn.unittestscanner.services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    RuleService ruleService;

    @Autowired
    AccountService accountService;

    @Autowired
    ScanningService scanningService;

    @Autowired
    EmailService emailService;

    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/ext-resources/";

    public static final String DIRECTORIES_PROJECT_PATH = UPLOAD_DIRECTORY + "projectDirectories/";

    @GetMapping(value = "/home")
    public String getHomePage(HttpSession httpSession) {
        if (accountService.isAlreadyLoggedIn(httpSession)){
            try {
                Optional<ProjectDTO> projectDTO = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

                if (projectDTO.isPresent()) {
                    projectService.deleteFileOrDirectory(projectDTO.get().getProjectPath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            projectService.deleteDataInStaticObject();

            return "home_page";
        } else {
            return "redirect:/login-ia";
        }
    }

    @GetMapping(value = "/upload")
    public String getUploadProjectPage(HttpSession httpSession) {
        if (accountService.isAlreadyLoggedIn(httpSession)){

            try {
                Optional<ProjectDTO> projectDTO = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

                if (projectDTO.isPresent()) {
                    projectService.deleteFileOrDirectory(projectDTO.get().getProjectPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            projectService.deleteDataInStaticObject();

            return "upload_project_page";
        } else {
            return "redirect:/login-ia";
        }
    }

    @GetMapping(value = "/upload-nu")
    public String getUploadProjectPageNotUploaded(Model model) {
        model.addAttribute("message", "The java project has not been uploaded. Please upload first!");

        return "upload_project_page-show_message";
    }

    @GetMapping(value = "/upload-wu")
    public String getUploadProjectPageWrongUpload(Model model) {
        model.addAttribute("message", "The project is wrong. Please re-upload the correct project!");

        return "upload_project_page-show_message";
    }

    @PostMapping(value = "/project/upload")
    public String uploadProject(@RequestParam("project") MultipartFile multipartFile, HttpSession httpSession) {

        String destinationUploadPath = UPLOAD_DIRECTORY + multipartFile.getOriginalFilename();

        projectService.saveFile(multipartFile, destinationUploadPath);

        Timestamp uploadTime = new Timestamp(System.currentTimeMillis());

        String directoryName = multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().indexOf(".zip"));

        String projectPath = DIRECTORIES_PROJECT_PATH + directoryName + "-uploadedAt-" + uploadTime + "-byAccount-" + httpSession.getAttribute("idAccount");

        projectService.unzipFile(destinationUploadPath, projectPath);

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setProjectPath(projectPath);

        StaticDataProjectDTO.getProjectDTOHashMap().put(httpSession.getAttribute("email").toString(), projectDTO);

        return "redirect:/scan-preparation";
    }

    @GetMapping(value = "/scan-preparation")
    public String getScanningPreparationPage() {
        return "scanning_preparation_page";
    }

    @GetMapping(value = "/project-info")
    public String getProjectInfoPage(HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        if (!projectService.isFileUploaded(httpSession)){
            return "redirect:/upload-nu";
        }

        ProjectDTO projectDTO = StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString());

        boolean isCorrectProject = projectService.collectProjectInformation(projectDTO);

        if (!isCorrectProject) {

            try {
                Optional<ProjectDTO> projectDTOOptional = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

                if (projectDTOOptional.isPresent()) {
                    projectService.deleteFileOrDirectory(projectDTOOptional.get().getProjectPath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            projectService.deleteDataInStaticObject();

            return "redirect:/upload-wu";
        }

        model.addAttribute("idProject", projectDTO.getIdProject());
        model.addAttribute("groupId", projectDTO.getGroupId());
        model.addAttribute("artifactId", projectDTO.getArtifactId());
        model.addAttribute("version", projectDTO.getVersion());
        model.addAttribute("numberUTClass", projectDTO.getNumberUTClass());
        model.addAttribute("listOfClassTest", projectDTO.getUtClassNameList());
        model.addAttribute("ruleGroup", new RuleGroupDTO());
        model.addAttribute("ruleGroupList", ruleService.getRuleByIdAccount((Long) httpSession.getAttribute("idAccount")));

        return "project_information_page";
    }

    @PostMapping(value = "/project-scan")
    public String scanProject(RuleGroupDTO ruleGroupDTO, HttpSession httpSession) {

        ProjectDTO projectDTO = StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString());

        File projectDirectory = new File(projectDTO.getProjectPath());

        long idProject = scanningService.scan(ruleGroupDTO.getRuleGroupName(), httpSession, projectDirectory, projectDTO);

        return "redirect:/scan-result?id=" + idProject;
    }

    @GetMapping(value = "/scan-result")
    public String getScanResult(@RequestParam long id, Model model) {

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id);

        model.addAttribute("project", projectDTO);

        return "list_wrong_class_page";
    }

    @GetMapping(value = "/project-list")
    public String getProjectList(HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        List<ProjectDTO> projectDTOList = projectService.getProjectList(httpSession);

        model.addAttribute("projectList", projectDTOList);

        return "list_of_project_page";
    }

    @GetMapping(value = "/class-list")
    public String getWrongClassList(@RequestParam long id, HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id);

        model.addAttribute("project", projectDTO);

        return "list_wrong_class_page";
    }

    @GetMapping(value = "/class-list-email-success")
    public String getWrongClassListEmailSuccess(@RequestParam long id, HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id);

        model.addAttribute("message", "Scan result has been sent to your email");
        model.addAttribute("project", projectDTO);

        return "list_wrong_class_page-show_message";
    }

    @GetMapping(value = "/class-list-email-failed")
    public String getWrongClassListEmailFailed(@RequestParam long id, HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id);

        model.addAttribute("message", "Sorry send scan result failed, email message cannot be sent because the connection is not stable!");
        model.addAttribute("project", projectDTO);

        return "list_wrong_class_page-show_message";
    }

    @GetMapping(value = "/send-result")
    public String sendScanningResult(@RequestParam Long idProject, HttpSession httpSession) {
        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        AccountDTO accountDTO = accountService.findAccountById((Long) httpSession.getAttribute("idAccount"));

        ProjectDTO projectDTO = projectService.getProject(idProject);

        String scanResultFilePath = projectService.findScanResultFileCreated(idProject);

        boolean isEmailSuccess = emailService.sendEmailScanResult(accountDTO, projectDTO, scanResultFilePath);

        if (isEmailSuccess) {
            return "redirect:/class-list-email-success?id=" + idProject;
        } else {
            return "redirect:/class-list-email-failed?id=" + idProject;
        }
    }

    @GetMapping(value = "/code-list")
    public String getWrongCodeList(@RequestParam List<Long> id, HttpSession httpSession, Model model) {

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id.get(0));

        WrongTestClassDTO wrongTestClassDTO = new WrongTestClassDTO();

        projectDTO.getWrongTestClassList().forEach(wrongTestClassDTOFromProjectDTO -> {
            if (wrongTestClassDTOFromProjectDTO.getIdWrongTestClass() == id.get(1)){
                List<WrongTestCodeDTO> wrongTestCodeDTOList = new ArrayList<>();

                wrongTestClassDTO.setIdWrongTestClass(wrongTestClassDTOFromProjectDTO.getIdWrongTestClass());
                wrongTestClassDTO.setClassName(wrongTestClassDTOFromProjectDTO.getClassName());
                wrongTestClassDTO.setClassPath(wrongTestClassDTOFromProjectDTO.getClassPath());

                wrongTestClassDTOFromProjectDTO.getWrongTestCodeList().forEach(wrongTestCode -> {

                    WrongTestCodeDTO wrongTestCodeDTO = new WrongTestCodeDTO();
                    wrongTestCodeDTO.setIdWrongTestCode(wrongTestCode.getIdWrongTestCode());
                    wrongTestCodeDTO.setCode(wrongTestCode.getCode());
                    wrongTestCodeDTO.setErrorMessage(wrongTestCode.getErrorMessage());
                    wrongTestCodeDTO.setLineNumber(wrongTestCode.getLineNumber());
                    wrongTestCodeDTOList.add(wrongTestCodeDTO);
                });

                wrongTestClassDTO.setWrongTestCodeList(wrongTestCodeDTOList);
                wrongTestClassDTO.setNumberOfWrongTestCode(wrongTestCodeDTOList.size());
            }
        });

        String allLineNumber = projectService.getAllLineNumber(wrongTestClassDTO);

        String sourceCode = projectService.getSourceCodeOfTestClass(projectDTO.getProjectPath(), wrongTestClassDTO.getClassPath());

        model.addAttribute("project", projectDTO);
        model.addAttribute("class", wrongTestClassDTO);
        model.addAttribute("allLineNumber", allLineNumber);
        model.addAttribute("sourceCode", sourceCode);

        return "list_of_wrong_code_page";
    }











    @GetMapping(value = "/class-list-all")
    public String getWrongClassListAllAccess(@RequestParam long id, Model model) {

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id);

        model.addAttribute("project", projectDTO);

        return "list_wrong_class_page-public";
    }

    @GetMapping(value = "/code-list-all")
    public String getDetailResultOfScanAllAccess(@RequestParam List<Long> id, Model model) {

        ProjectDTO projectDTO = projectService.getProjectAndChildByIdProject(id.get(0));

        WrongTestClassDTO wrongTestClassDTO = new WrongTestClassDTO();

        projectDTO.getWrongTestClassList().forEach(wrongTestClassDTOFromProjectDTO -> {
            if (wrongTestClassDTOFromProjectDTO.getIdWrongTestClass() == id.get(1)){
                List<WrongTestCodeDTO> wrongTestCodeDTOList = new ArrayList<>();

                wrongTestClassDTO.setIdWrongTestClass(wrongTestClassDTOFromProjectDTO.getIdWrongTestClass());
                wrongTestClassDTO.setClassName(wrongTestClassDTOFromProjectDTO.getClassName());
                wrongTestClassDTO.setClassPath(wrongTestClassDTOFromProjectDTO.getClassPath());

                wrongTestClassDTOFromProjectDTO.getWrongTestCodeList().forEach(wrongTestCode -> {

                    WrongTestCodeDTO wrongTestCodeDTO = new WrongTestCodeDTO();
                    wrongTestCodeDTO.setIdWrongTestCode(wrongTestCode.getIdWrongTestCode());
                    wrongTestCodeDTO.setCode(wrongTestCode.getCode());
                    wrongTestCodeDTO.setErrorMessage(wrongTestCode.getErrorMessage());
                    wrongTestCodeDTO.setLineNumber(wrongTestCode.getLineNumber());
                    wrongTestCodeDTOList.add(wrongTestCodeDTO);
                });

                wrongTestClassDTO.setWrongTestCodeList(wrongTestCodeDTOList);
                wrongTestClassDTO.setNumberOfWrongTestCode(wrongTestCodeDTOList.size());
            }
        });

        String allLineNumber = projectService.getAllLineNumber(wrongTestClassDTO);

        String sourceCode = projectService.getSourceCodeOfTestClass(projectDTO.getProjectPath(), wrongTestClassDTO.getClassPath());

        model.addAttribute("project", projectDTO);
        model.addAttribute("class", wrongTestClassDTO);
        model.addAttribute("allLineNumber", allLineNumber);
        model.addAttribute("sourceCode", sourceCode);

        return "list_of_wrong_code_page-public";
    }


}

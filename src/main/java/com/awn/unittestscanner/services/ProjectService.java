package com.awn.unittestscanner.services;

import com.awn.unittestscanner.helper.DirectoryService;
import com.awn.unittestscanner.dtos.*;
import com.awn.unittestscanner.entities.*;
import com.awn.unittestscanner.repositories.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    private static final int BUFFER_SIZE = 4096;

    private static final String TEMPORARY_PATH = "/__MACOSX";

    private static final String TARGET_PATH = "/target";

    public boolean isFileUploaded(HttpSession httpSession) {
        ProjectDTO projectDTO = StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString());

        if (projectDTO != null) {
            return true;
        } else {
            return false;
        }
    }

    public void saveFile(MultipartFile multipartFile, String destinationUploadPath) {
        try {
            File file = new File(destinationUploadPath);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzipFile(String destinationUploadPath, String destinationDirectoryPath) {
        File destinationDirectory = new File(destinationDirectoryPath);
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        ZipInputStream zipInput = null;
        try {
            zipInput = new ZipInputStream(new FileInputStream(destinationUploadPath));

            ZipEntry zipEntry = zipInput.getNextEntry();
            // iterates over entries in the zip file
            while (zipEntry != null) {
                String filePath = destinationDirectoryPath + File.separator + zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    // if the zipEntry is a file, extracts it
                    extractProject(zipInput, filePath);
                } else {
                    // if the zipEntry is a directory, make the directory
                    File directoryEntry = new File(filePath);
                    directoryEntry.mkdir();
                }
                zipInput.closeEntry();
                zipEntry = zipInput.getNextEntry();
            }
            zipInput.close();

            deleteFileOrDirectory(destinationUploadPath);

            String temporaryFile = destinationDirectoryPath + TEMPORARY_PATH;

            deleteFileOrDirectory(temporaryFile);

            String targetDirectory = destinationDirectoryPath + destinationDirectoryPath.substring(destinationDirectoryPath.lastIndexOf("/"), destinationDirectoryPath.indexOf("-uploadedAt-")) + TARGET_PATH;

            deleteFileOrDirectory(targetDirectory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractProject(ZipInputStream zipFile, String filePath) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));

            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipFile.read(bytesIn)) != -1) {
                bufferedOutputStream.write(bytesIn, 0, read);
            }
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFileOrDirectory(String projectDirectoryPath) throws IOException {

        Path directory = Paths.get(projectDirectoryPath);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Files.delete(file); // this will work because it's always a File
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir); //this will work because Files in the directory are already deleted
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public boolean collectProjectInformation(ProjectDTO projectDTO) {
        List<File> pomFileList;
        List<File> testFileList;
        File project = new File(projectDTO.getProjectPath());
        DirectoryService directoryService = new DirectoryService();
        pomFileList = directoryService.findPomFiles(project);



        for (File pomFile : pomFileList) {
            NodeList root = getRootNodeListFromPom(pomFile);
            Node projectNode = getNode("project", root);
            if (checkMainPomFile(pomFileList, projectNode)) {
//            if (checkMainPomFile(pomFileList, pomFile)) {
                findGroupIdInPom(projectNode, projectDTO);
                findArtifactIdInPom(projectNode, projectDTO);
                findVersionInPom(projectNode, projectDTO);
                testFileList = directoryService.findTestFiles(project);
                projectDTO.setNumberUTClass(testFileList.size());
                List<String> utClassNameList = new ArrayList<>();
                for (File testFile : testFileList) {
                    utClassNameList.add(testFile.getAbsolutePath().substring(testFile.getAbsolutePath().lastIndexOf("/") + 1));
                }
                projectDTO.setUtClassNameList(utClassNameList);
            }
        }

        return !pomFileList.isEmpty();
    }

    private void findGroupIdInPom(Node projectNode, ProjectDTO projectDTO) {
        NodeList projectChildNodes = projectNode.getChildNodes();

        if (!getNodeValue("groupId", projectChildNodes).equals("")) {
            projectDTO.setGroupId(getNodeValue("groupId", projectChildNodes));
        } else {
            Node parentNode = getNode("parent", projectChildNodes);
            NodeList parentChildNodes = parentNode.getChildNodes();
            projectDTO.setGroupId(getNodeValue("groupId", parentChildNodes));
        }
    }

    private void findArtifactIdInPom(Node projectNode, ProjectDTO projectDTO) {
        NodeList projectChildNodes = projectNode.getChildNodes();

        if (!getNodeValue("artifactId", projectChildNodes).equals("")) {
            projectDTO.setArtifactId(getNodeValue("artifactId", projectChildNodes));
        } else {
            Node parentNode = getNode("parent", projectChildNodes);
            NodeList parentChildNodes = parentNode.getChildNodes();
            projectDTO.setArtifactId(getNodeValue("artifactId", parentChildNodes));
        }
    }

    private void findVersionInPom(Node projectNode, ProjectDTO projectDTO) {
        NodeList projectChildNodes = projectNode.getChildNodes();

        if (!getNodeValue("version", projectChildNodes).equals("")) {
            projectDTO.setVersion(getNodeValue("version", projectChildNodes));
        } else {
            Node parentNode = getNode("parent", projectChildNodes);
            NodeList parentChildNodes = parentNode.getChildNodes();
            projectDTO.setVersion(getNodeValue("version", parentChildNodes));
        }
    }


    private NodeList getRootNodeListFromPom(File pomFile) {
        NodeList root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = null;

            document = dBuilder.parse(pomFile);

            document.getDocumentElement().normalize();

            root = document.getChildNodes();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public boolean checkMainPomFile(List<File> pomFileList, Node projectNode) {
        boolean mainPom = false;
        if (pomFileList.size() > 1) {
            for (int i = 0; i < projectNode.getChildNodes().getLength(); i++) {
                if (projectNode.getChildNodes().item(i).getNodeName().equals("modules")) {
                    mainPom = true;
                }
            }
        } else {
            mainPom = true;
        }
        return mainPom;
    }

//    public boolean checkMainPomFile(List<File> pomFileList, File pomFile) {
//        boolean mainPom = false;
//        int countCharacter = 999;
//        String mainPomFilePath = "";
//        for (File pom : pomFileList) {
//            int countSlash = StringUtils.countMatches(pom.getPath(), "/");
//            if (countCharacter > countSlash) {
//                countCharacter = countSlash;
//                mainPomFilePath = pom.getPath();
//            }
//        }
//        if (pomFileList.size() > 1) {
//            System.out.println(mainPomFilePath);
//            System.out.println(countCharacter);
//            if (mainPomFilePath.equalsIgnoreCase(pomFile.getPath())) {
//                mainPom = true;
//            }
//        } else {
//            mainPom = true;
//        }
//        return mainPom;
//    }
//
//    public boolean checkMainPomFile(List<File> pomFileList, File pomFile) {
//        boolean mainPom = false;
//        int countCharacter = 999;
//        String mainPomFilePath = "";
//        for (File pom : pomFileList) {
//            int countSlash = StringUtils.countMatches(pom.getPath(), "/");
//            if (countCharacter > countSlash) {
//                countCharacter = countSlash;
//                mainPomFilePath = pom.getPath();
//            }
//        }
//        if (pomFileList.size() > 1) {
//            System.out.println(mainPomFilePath);
//            System.out.println(countCharacter);
//            if (mainPomFilePath.equalsIgnoreCase(pomFile.getPath())) {
//                mainPom = true;
//            }
//        } else {
//            mainPom = true;
//        }
//        return mainPom;
//    }

//    public boolean checkMainPomFile(List<File> pomFileList, Node projectNode) {
//        boolean mainPom = false;
//        int countCharacter = 0;
//        if (pomFileList.size() > 1) {
//            for (File pomFile : pomFileList) {
//                int character = StringUtils.countMatches(pomFile.getPath(), "/");
//                if (countCharacter > character) {
//                    countCharacter = character;
//                }
//            }
//            for (int i = 0; i < projectNode.getChildNodes().getLength(); i++) {
//                if (projectNode.getChildNodes().item(i).getNodeName().equals("modules") && ) {
//                    mainPom = true;
//                }
//            }
//        } else {
//            mainPom = true;
//        }
//        return mainPom;
//    }

    private Node getNode(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }
        return null;
    }

    private String getNodeValue(String tagXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node innerNode = nodeList.item(i);
            if (innerNode.getNodeName().equalsIgnoreCase(tagXml)) {
                NodeList childInnerNodeList = innerNode.getChildNodes();
                for (int j = 0; j < childInnerNodeList.getLength(); j++) {
                    Node nodeData = childInnerNodeList.item(j);
                    if (nodeData.getNodeType() == Node.TEXT_NODE)
                        return nodeData.getNodeValue();
                }
            }
        }
        return "";
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setIdProject(project.getIdProject());
        projectDTO.setGroupId(project.getGroupId());
        projectDTO.setArtifactId(project.getArtifactId());
        projectDTO.setVersion(project.getVersion());
        projectDTO.setNumberUTClass(project.getNumberUTClass());
        projectDTO.setScanTime(project.getScanTime());
        projectDTO.setProjectPath(project.getProjectPath());
        return projectDTO;
    }

    public ProjectDTO getProjectAndChildByIdProject(long idProject) {
        ProjectDTO projectDTO = new ProjectDTO();
        Project project = projectRepository.getOne(idProject);
        List<WrongTestClassDTO> wrongTestClassDTOList = new ArrayList<>();
        projectDTO.setIdProject(project.getIdProject());
        projectDTO.setGroupId(project.getGroupId());
        projectDTO.setArtifactId(project.getArtifactId());
        projectDTO.setVersion(project.getVersion());
        projectDTO.setScanTime(project.getScanTime());
        projectDTO.setProjectPath(project.getProjectPath());
        projectDTO.setNumberUTClass(project.getNumberUTClass());
        project.getWrongTestClassList().forEach(wrongTestClass -> {

            List<WrongTestCodeDTO> wrongTestCodeDTOList = new ArrayList<>();
            WrongTestClassDTO wrongTestClassDTO = new WrongTestClassDTO();
            wrongTestClassDTO.setIdWrongTestClass(wrongTestClass.getIdWrongTestClass());
            wrongTestClassDTO.setClassName(wrongTestClass.getClassName());
            wrongTestClassDTO.setClassPath(wrongTestClass.getClassPath());

            wrongTestClass.getWrongTestCodeList().forEach(wrongTestCode -> {

                WrongTestCodeDTO wrongTestCodeDTO = new WrongTestCodeDTO();
                wrongTestCodeDTO.setIdWrongTestCode(wrongTestCode.getIdWrongTestCode());
                wrongTestCodeDTO.setCode(wrongTestCode.getCode());
                wrongTestCodeDTO.setErrorMessage(wrongTestCode.getErrorMessage());
                wrongTestCodeDTO.setLineNumber(wrongTestCode.getLineNumber());
                wrongTestCodeDTOList.add(wrongTestCodeDTO);
            });

            wrongTestClassDTO.setWrongTestCodeList(wrongTestCodeDTOList);
            wrongTestClassDTO.setNumberOfWrongTestCode(wrongTestCodeDTOList.size());
            wrongTestClassDTOList.add(wrongTestClassDTO);
        });

        projectDTO.setWrongTestClassList(wrongTestClassDTOList);

        return projectDTO;
    }

    public String getAllLineNumber(WrongTestClassDTO wrongTestClassDTO) {
        StringBuilder lineNumber = new StringBuilder();

        for (WrongTestCodeDTO wrongTestCodeDTO : wrongTestClassDTO.getWrongTestCodeList()) {

            lineNumber.append(wrongTestCodeDTO.getLineNumber()).append(", ");

        }

        return lineNumber.toString();
    }

    public String getSourceCodeOfTestClass(String directoryPath, String testClassPathPartial) {

        DirectoryService directoryService = new DirectoryService();

        File directoryProject = new File(directoryPath);

        String testClassPathFull = directoryService.findTestFilePath(directoryProject, testClassPathPartial);

        FileInputStream fileInputStream = null;

        StringBuilder stringBuilder = new StringBuilder(512);

        try {
            fileInputStream = new FileInputStream(new File(testClassPathFull));
            Reader reader = new InputStreamReader(fileInputStream, "UTF-8");
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    public List<ProjectDTO> getProjectList(HttpSession httpSession) {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        Optional<List<Project>> optionalProjectList = projectRepository.findAllByAccount_IdAccount((Long) httpSession.getAttribute("idAccount"));

        if (optionalProjectList.isPresent()) {
            optionalProjectList.get().forEach(project -> {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setIdProject(project.getIdProject());
                projectDTO.setGroupId(project.getGroupId());
                projectDTO.setArtifactId(project.getArtifactId());
                projectDTO.setVersion(project.getVersion());
                projectDTO.setNumberUTClass(project.getNumberUTClass());
                projectDTO.setScanTime(project.getScanTime());

                RuleGroup ruleGroup = project.getRuleGroup();

                RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
                ruleGroupDTO.setIdRuleGroup(ruleGroup.getIdRuleGroup());
                ruleGroupDTO.setRuleGroupName(ruleGroup.getRuleGroupName());
                ruleGroupDTO.setRuleGroupDescription(ruleGroup.getRuleGroupDescription());

                List<RuleDTO> ruleDTOList = new ArrayList<>();

                for (Rule rule : ruleGroup.getRuleList()) {
                    RuleDTO ruleDTO = new RuleDTO();
                    ruleDTO.setIdRule(rule.getIdRule());
                    ruleDTO.setRuleName(rule.getRuleName());
                    ruleDTO.setRuleDescription(rule.getRuleDescription());
                    ruleDTO.setRuleSyntax(rule.getRuleSyntax());
                    ruleDTOList.add(ruleDTO);
                }

                ruleGroupDTO.setRuleList(ruleDTOList);

                projectDTO.setRuleGroup(ruleGroupDTO);
                projectDTOList.add(projectDTO);
            });
        }

        return projectDTOList;
    }

    public void deleteDataInStaticObject() {
        StaticDataProjectDTO.getProjectDTOHashMap().clear();
    }

    public String findScanResultFileCreated(Long idProject) {
        String path = null;
        Optional<Project> project = projectRepository.findById(idProject);
        if (project.isPresent()) {
            DirectoryService directoryService = new DirectoryService();
            String scanResultFile = directoryService.findScanResultFilePath(new File(project.get().getProjectPath()));
            if (scanResultFile != null) {
                path = scanResultFile;
            } else {
                createScanResultFile(idProject);
                path = directoryService.findScanResultFilePath(new File(project.get().getProjectPath()));
            }
        }
        return path;
    }

    private void createScanResultFile(long idProject) {

        String[] projectInfo = {"GroupID : ", "ArtifactID : ", "Version : ", "Scan Time : ", "Team Leader : ", "Link Project Scan Result : "};

        String[] columns = {"Class Name", "Class Path", "Wrong Code", "Line Number", "Error Message"};

        Project project = projectRepository.getOne(idProject);

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

            /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper creationHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Scan Result");

        Font projectInfoFont = workbook.createFont();
        projectInfoFont.setBold(true);
        projectInfoFont.setFontHeightInPoints((short) 14);
        projectInfoFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle projectInfoStyle = workbook.createCellStyle();
        projectInfoStyle.setFont(projectInfoFont);
        projectInfoStyle.setAlignment(HorizontalAlignment.LEFT);
        projectInfoStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Create Project Info Row
        for (int row = 0; row < projectInfo.length; row++) {
            Row projectInformationRow = sheet.createRow(row);

            Cell projectInformationTitleCell = projectInformationRow.createCell(0);
            projectInformationTitleCell.setCellValue(projectInfo[row]);
            projectInformationTitleCell.setCellStyle(projectInfoStyle);

            Cell projectInformationValueCell = projectInformationRow.createCell(1);
            if (row == 0) {
                projectInformationValueCell.setCellValue(project.getGroupId());
                projectInformationValueCell.setCellStyle(projectInfoStyle);
            } else if (row == 1) {
                projectInformationValueCell.setCellValue(project.getArtifactId());
                projectInformationValueCell.setCellStyle(projectInfoStyle);
            } else if (row == 2) {
                projectInformationValueCell.setCellValue(project.getVersion());
                projectInformationValueCell.setCellStyle(projectInfoStyle);
            } else if (row == 3) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(projectInfoFont);
                cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
                cellStyle.setAlignment(HorizontalAlignment.LEFT);

                projectInformationValueCell.setCellValue(project.getScanTime());
                projectInformationValueCell.setCellStyle(cellStyle);
            } else if (row == 4) {
                projectInformationValueCell.setCellValue(project.getAccount().getFullName());
                projectInformationValueCell.setCellStyle(projectInfoStyle);
            } else if (row == 5) {
                Font font = workbook.createFont();
                font.setUnderline(XSSFFont.U_SINGLE);
                font.setColor(IndexedColors.BLUE.getIndex());
                font.setFontHeightInPoints((short) 14);

                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(font);

                XSSFHyperlink link = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.URL);
                link.setAddress("http://localhost:8080/class-list-all?id=" + project.getIdProject());
                projectInformationValueCell.setHyperlink((XSSFHyperlink) link);
                projectInformationValueCell.setCellStyle(cellStyle);
                projectInformationValueCell.setCellValue("click_me!");
            }
        }

        // Create a CellStyle with the font
        CellStyle headerTableStyle = workbook.createCellStyle();
        headerTableStyle.setFont(projectInfoFont);
        headerTableStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Create a Row
        Row headerTableRow = sheet.createRow(7);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerTableRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerTableStyle);
        }

        // Create Other rows and cells with employees data
        int rowNum = 8;

        for (WrongTestClass wrongTestClass : project.getWrongTestClassList()) {

            for (WrongTestCode wrongTestCode : wrongTestClass.getWrongTestCodeList()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(wrongTestClass.getClassName());
                row.createCell(1).setCellValue(wrongTestClass.getClassPath());
                row.createCell(2).setCellValue(wrongTestCode.getCode());
                row.createCell(3).setCellValue(wrongTestCode.getLineNumber());
                row.createCell(4).setCellValue(wrongTestCode.getErrorMessage());
            }

        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(new File(project.getProjectPath() + File.separator + "ScanResult.xlsx"));
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ProjectDTO getProject(long idProject) {
        Project project = projectRepository.getOne(idProject);

        return convertToProjectDTO(project);
    }


}

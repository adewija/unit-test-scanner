package com.awn.unittestscanner.testData5;

import com.awn.unittestscanner.dtos.ProjectDTO;
import com.awn.unittestscanner.entities.*;
import com.awn.unittestscanner.repositories.ProjectRepository;
import com.awn.unittestscanner.services.ProjectService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Before
    public void setUp(){
        initMocks(this);
    }

    @After
    public void tearDown(){
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void collectProjectInformationPath1Test(){
        String path = "/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData1/path1";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectPath(path);
        boolean result = projectService.collectProjectInformation(projectDTO);
        assertFalse(result);
    }

    @Test
    public void collectProjectInformationPath2Test(){
        String path = "/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData1/path2";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectPath(path);
        boolean result = projectService.collectProjectInformation(projectDTO);
        assertTrue(result);
    }

    @Test
    public void collectProjectInformationPath3Test(){
        String path = "/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData1/path3";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectPath(path);
        boolean result = projectService.collectProjectInformation(projectDTO);

        assertTrue(result);
        assertEquals("com.awn.x", projectDTO.getGroupId());
        assertEquals("visio", projectDTO.getArtifactId());
        assertEquals("5.6.0-3-SNAPSHOT", projectDTO.getVersion());
        assertEquals(0, projectDTO.getNumberUTClass());
    }

    @Test
    public void collectProjectInformationPath4Test(){
        String path = "/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData1/path4";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectPath(path);
        boolean result = projectService.collectProjectInformation(projectDTO);

        assertTrue(result);
        assertEquals("com.awn.x", projectDTO.getGroupId());
        assertEquals("visio", projectDTO.getArtifactId());
        assertEquals("5.6.0-3-SNAPSHOT", projectDTO.getVersion());
        assertEquals(1, projectDTO.getNumberUTClass());
        assertEquals("ParsingServiceTest.java", projectDTO.getUtClassNameList().get(0));
    }

    @Test
    public void checkMainPomFilePath1Test(){
        File file1 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path1/pom.xml");

        List<File> fileList = new ArrayList<>();
        fileList.add(file1);

        NodeList root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = null;
            document = dBuilder.parse(file1);
            document.getDocumentElement().normalize();
            root = document.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Node projectNode = null;

        for (int x = 0; x < root.getLength(); x++) {
            Node node = root.item(x);
            if (node.getNodeName().equalsIgnoreCase("project")) {
                projectNode = node;
            }
        }

        boolean result = projectService.checkMainPomFile(fileList,projectNode);

        assertTrue(result);
    }

    @Test
    public void checkMainPomFilePath2Test(){
        File file1 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path2/mainPom/pom.xml");
        File file2 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path2/childPom/pom.xml");

        List<File> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);

        NodeList root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = null;
            document = dBuilder.parse(file1);
            document.getDocumentElement().normalize();
            root = document.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Node projectNode = null;

        for (int x = 0; x < root.getLength(); x++) {
            Node node = root.item(x);
            if (node.getNodeName().equalsIgnoreCase("project")) {
                projectNode = node;
            }
        }

        boolean result = projectService.checkMainPomFile(fileList,projectNode);

        assertFalse(result);
    }

    @Test
    public void checkMainPomFilePath3Test(){
        File file1 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path3/childPom/pom.xml");
        File file2 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path3/mainPom/pom.xml");

        List<File> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);

        NodeList root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = null;
            document = dBuilder.parse(file1);
            document.getDocumentElement().normalize();
            root = document.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Node projectNode = null;

        for (int x = 0; x < root.getLength(); x++) {
            Node node = root.item(x);
            if (node.getNodeName().equalsIgnoreCase("project")) {
                projectNode = node;
            }
        }

        boolean result = projectService.checkMainPomFile(fileList,projectNode);

        assertFalse(result);
    }

    @Test
    public void checkMainPomFilePath4Test(){
        File file1 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path4/mainPom/pom.xml");
        File file2 = new File("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData2/path4/childPom/pom.xml");

        List<File> fileList = new ArrayList<>();
        fileList.add(file1);
        fileList.add(file2);

        NodeList root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = null;
            document = dBuilder.parse(file1);
            document.getDocumentElement().normalize();
            root = document.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Node projectNode = null;

        for (int x = 0; x < root.getLength(); x++) {
            Node node = root.item(x);
            if (node.getNodeName().equalsIgnoreCase("project")) {
                projectNode = node;
            }
        }

        boolean result = projectService.checkMainPomFile(fileList,projectNode);

        assertTrue(result);
    }

    @Test
    public void findScanResultFileCreatedPath1Test(){
        long idProject = 111;

        Optional<Project> optionalProject = Optional.ofNullable(null);

        when(projectRepository.findById(idProject)).thenReturn(optionalProject);

        String resultActual = projectService.findScanResultFileCreated(idProject);

        verify(projectRepository).findById(idProject);

        assertNull(resultActual);
    }

    @Test
    public void findScanResultFileCreatedPath2Test(){
        Project project = new Project();
        project.setIdProject(111);
        project.setProjectPath("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData4/path2");

        Optional<Project> optionalProject = Optional.of(project);

        when(projectRepository.findById(project.getIdProject())).thenReturn(optionalProject);

        String resultActual = projectService.findScanResultFileCreated(project.getIdProject());

        verify(projectRepository).findById(project.getIdProject());

        assertEquals("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData4/path2/ScanResult.xlsx", resultActual);
    }

    @Test
    public void findScanResultFileCreatedPath3Test(){
        Account account = new Account();
        WrongTestCode wrongTestCode = new WrongTestCode();
        List<WrongTestCode> wrongTestCodeList = new ArrayList<>();
        wrongTestCodeList.add(wrongTestCode);

        WrongTestClass wrongTestClass = new WrongTestClass();
        List<WrongTestClass> wrongTestClassList = new ArrayList<>();
        wrongTestClass.setClassName("ClassName");
        wrongTestClass.setClassPath("ClassPath");
        wrongTestClass.setIdWrongTestClass(1);
        wrongTestClass.setWrongTestCodeList(wrongTestCodeList);
        wrongTestClassList.add(wrongTestClass);

        RuleGroup ruleGroup = new RuleGroup();

        Project project = new Project();
        project.setIdProject(111);
        project.setGroupId("GroupId");
        project.setArtifactId("ArtifactId");
        project.setNumberUTClass(0);
        project.setProjectPath("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData4/path3");
        project.setVersion("Version");
        project.setScanTime(new Timestamp(System.currentTimeMillis()));
        project.setRuleGroup(ruleGroup);
        project.setAccount(account);
        project.setWrongTestClassList(wrongTestClassList);
        project.setWrongTestCodeList(wrongTestCodeList);


        Optional<Project> optionalProject = Optional.of(project);

        when(projectRepository.findById(project.getIdProject())).thenReturn(optionalProject);
        when(projectRepository.getOne(project.getIdProject())).thenReturn(project);

        String resultActual = projectService.findScanResultFileCreated(project.getIdProject());

        verify(projectRepository).findById(project.getIdProject());
        verify(projectRepository).getOne(project.getIdProject());

        assertEquals("/Users/adewijanugraha/Google Drive/SKRIPSI/Produk/unit-test-scanner/src/test/java/com/awn/unittestscanner/testData4/path3/ScanResult.xlsx", resultActual);

        Path fileScanResult = Paths.get(resultActual);

        try {
            Files.walkFileTree(fileScanResult, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file); // this will work because it's always a File
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

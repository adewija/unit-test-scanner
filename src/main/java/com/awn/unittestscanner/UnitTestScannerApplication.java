package com.awn.unittestscanner;

import com.awn.unittestscanner.controllers.ProjectController;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class UnitTestScannerApplication {

	public static void main(String[] args) {
		new File(ProjectController.UPLOAD_DIRECTORY).mkdirs();
		new File(ProjectController.UPLOAD_DIRECTORY + "/projectDirectories/").mkdirs();
		SpringApplication.run(UnitTestScannerApplication.class, args);
	}

}

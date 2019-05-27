package com.awn.unittestscanner.helper;

import com.awn.unittestscanner.dtos.AccountDTO;
import com.awn.unittestscanner.dtos.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Controller
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendEmailRegisterValidation(AccountDTO accountDTO) {
        try {
            System.out.println("sending to  " + accountDTO.getEmail());
            System.out.println("name  " + accountDTO.getFullName());
            System.out.println(accountDTO.getIdAccount());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("idAccount", accountDTO.getIdAccount());
            context.setVariable("fullName", accountDTO.getFullName());

            helper.setTo(accountDTO.getEmail());
            helper.setText(templateEngine.process("email-register_validation", context), true);
            helper.setSubject("Validation Account to Registration in Unit Test Scanner");
            javaMailSender.send(message);

            System.out.println("Email Sent!");
            return true;
        } catch (Exception ex) {
            System.out.println("failed cause " + ex);
            return false;
        }
    }

    public boolean sendEmailUpdateValidation(AccountDTO accountDTO) {
        try {
            System.out.println("sending to  " + accountDTO.getEmail());
            System.out.println("name  " + accountDTO.getFullName());
            System.out.println(accountDTO.getIdAccount());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("idAccount", accountDTO.getIdAccount());
            context.setVariable("fullName", accountDTO.getFullName());

            helper.setTo(accountDTO.getEmail());
            helper.setText(templateEngine.process("email-update_account_validation", context), true);
            helper.setSubject("Validation Account to Update Profile in Unit Test Scanner");
            javaMailSender.send(message);

            System.out.println("Email Sent!");
            return true;
        } catch (Exception ex) {
            System.out.println("failed cause " + ex);
            return false;
        }
    }

    public boolean sendEmailUpdatePassword(AccountDTO accountDTO) {
        try {
            System.out.println("sending to  " + accountDTO.getEmail());
            System.out.println("name  " + accountDTO.getFullName());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("email", accountDTO.getEmail());
            context.setVariable("fullName", accountDTO.getFullName());

            helper.setTo(accountDTO.getEmail());
            helper.setText(templateEngine.process("email-update_password", context), true);
            helper.setSubject("Update Password in Unit Test Scanner");
            javaMailSender.send(message);

            System.out.println("Email Sent!");
            return true;
        } catch (Exception ex) {
            System.out.println("failed cause " + ex);
            return false;
        }
    }


    public boolean sendEmailScanResult(AccountDTO accountDTO, ProjectDTO projectDTO, String scanResultFilePath) {
        try {
            System.out.println("sending to  " + accountDTO.getEmail());
            System.out.println("name  " + accountDTO.getFullName());
            System.out.println("Project Name : " + projectDTO.getArtifactId());
            System.out.println("File path : " + scanResultFilePath);

            if (scanResultFilePath == null) {
                return false;
            }

            String projectName = projectDTO.getArtifactId().substring(0, 1).toUpperCase() + projectDTO.getArtifactId().substring(1);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("email", accountDTO.getEmail());
            context.setVariable("fullName", accountDTO.getFullName());

            helper.setTo(accountDTO.getEmail());
            helper.setText(templateEngine.process("email-send_scan_result", context), true);
            helper.setSubject("Scan Result of Unit Test Code from " + projectName + " Project");
            helper.addAttachment(projectName + "ScanResult.xlsx", new File(scanResultFilePath));
            javaMailSender.send(message);

            System.out.println("Email Sent!");
            return true;
        } catch (Exception ex) {
            System.out.println("failed cause " + ex);
            return false;
        }
    }

}
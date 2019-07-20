package com.awn.unittestscanner.controllers;

import com.awn.unittestscanner.dtos.AccountDTO;
import com.awn.unittestscanner.dtos.ProjectDTO;
import com.awn.unittestscanner.dtos.StaticDataProjectDTO;
import com.awn.unittestscanner.services.AccountService;
import com.awn.unittestscanner.helper.EmailService;
import com.awn.unittestscanner.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    ProjectService projectService;

//    @Autowired
//    EmailService emailService;

    @GetMapping(value = "/")
    public String landingPage(){
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model){
        model.addAttribute("account", new AccountDTO());
        return "login_page";
    }

    @GetMapping(value = "/login-nr")
    public String getLoginPageNotRegistered(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Your account is not registered. Please check your email and password again!");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-ia")
    public String getLoginPageIllegalAccess(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Illegal Accesss! Please login first!");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-ar")
    public String getLoginPageAfterRegister(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Please check your email and click the validate account button to validate your account!");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-arf")
    public String getLoginPageAfterRegisterFailed(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Registration Failed!\nYour email has been registered! Please select forget password if you forget your password.");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-afp")
    public String getLoginPageAfterForgetPassword(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Please check your email and click the update password button to update your password!");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-ups")
    public String getLoginPageUpdatePasswordSuccess(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Update Password Success!\nYour password has been successfully updated!");
        return "login_page-show_message";
    }

    @GetMapping(value = "/login-upia")
    public String getLoginPageUpdatePasswordIllegalAccess(Model model){
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Illegal Accesss! You cannot update your password. Your password has been changed!");
        return "login_page-show_message";
    }

    @PostMapping(value = "/account/login")
    public String loginAccount(HttpSession httpSession, AccountDTO accountDTO){
        AccountDTO accountDTOFromEntity = accountService.findAccount(accountDTO);
        if (accountDTOFromEntity != null){
            httpSession.setAttribute("idAccount", accountDTOFromEntity.getIdAccount());
            httpSession.setAttribute("email", accountDTOFromEntity.getEmail());
            httpSession.setAttribute("fullName", accountDTOFromEntity.getFullName());
            return "redirect:/home";
        } else {
            return "redirect:/login-nr";
        }
    }

    @GetMapping(value = "/account/logout")
    public String logoutAccount(HttpSession httpSession){

        if (!httpSession.isNew()){
            try {
                Optional<ProjectDTO> projectDTO = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

                if (projectDTO.isPresent()) {
                    projectService.deleteFileOrDirectory(projectDTO.get().getProjectPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            projectService.deleteDataInStaticObject();

            httpSession.invalidate();

        } else {
            httpSession.invalidate();
        }

        return "redirect:/login";
    }


//    -------------------------- Update Password --------------------------


    @GetMapping(value = "/forget-password")
    public String getSendEmailInUpdatePasswordPage(Model model){
        model.addAttribute("newAccount", new AccountDTO());
        return "send_email_in_update_password_page";
    }

    @GetMapping(value = "/forget-password-f")
    public String getSendEmailInUpdatePasswordPageFailed(Model model){
        model.addAttribute("newAccount", new AccountDTO());
        model.addAttribute("message", "Please check your email, your email is not registered!");
        return "send_email_in_update_password_page-show_message";
    }

    @GetMapping(value = "/forget-password-email-f")
    public String getSendEmailInUpdatePasswordPageEmailFailed(Model model){
        model.addAttribute("newAccount", new AccountDTO());
        model.addAttribute("message", "Sorry update password failed, email message cannot be sent because the connection is not stable!");
        return "send_email_in_update_password_page-show_message";
    }

    @PostMapping(value = "/update-password-send-email")
    public String sendEmailInUpdatePassword(AccountDTO accountDTO){
        AccountDTO accountDTOFromDB = accountService.checkEmail(accountDTO.getEmail());
        if (accountDTOFromDB == null){
            return "redirect:/forget-password-f";
        } else {
            return "redirect:/update-password?data=" + accountDTOFromDB.getFullName() + "&data=" + accountDTOFromDB.getEmail();

//            boolean isEmailSuccess = emailService.sendEmailUpdatePassword(accountDTOFromDB);
//            if (isEmailSuccess) {
//                return "redirect:/login-afp";
//            } else {
//                accountService.updatePasswordStatusBecauseEmailError(accountDTOFromDB);
//                return "redirect:/forget-password-email-f";
//            }

        }

    }

    @GetMapping(value = "/update-password")
    public String getUpdatePasswordPage(@RequestParam List<String> data, Model model){
        model.addAttribute("newAccount", new AccountDTO());
        model.addAttribute("fullName", data.get(0));
        model.addAttribute("email", data.get(1));

        return "update_password_page";
    }

    @PostMapping(value = "/account-update-password")
    public String updatePassword(@RequestParam String email, AccountDTO accountDTO, Model model){
        boolean isUpdated = accountService.updatePassword(email, accountDTO.getPassword());
        if (isUpdated) {
            return "redirect:/login-ups";
        } else {
            return "redirect:/login-upia";
        }
    }

    
//    -------------------------- Register --------------------------


    @GetMapping(value = "/register")
    public String getRegisterPage(Model model){
        model.addAttribute("newAccount", new AccountDTO());
        return "register_page";
    }

    @PostMapping(value = "/register-send-email")
    public String sendEmailInRegister(AccountDTO accountDTO, Model model){
        AccountDTO accountDTOSaved = accountService.saveAccount(accountDTO);

        if (accountDTOSaved == null) {
            return "redirect:/login-arf";
        }

        return "redirect:/account-validate?id=" + accountDTOSaved.getIdAccount();


//        boolean isEmailSuccess = emailService.sendEmailRegisterValidation(accountDTOSaved);
//
//        if (isEmailSuccess) {
//            return "redirect:/login-ar";
//        } else {
//            accountService.deleteAccountBeacuseEmailError(accountDTOSaved.getIdAccount());
//            model.addAttribute("newAccount", new AccountDTO());
//            return "register_page-show_message";
//        }
    }

    @GetMapping(value = "/account-validate")
    public String registerAccount(@RequestParam long id){
        accountService.validateAccount(id);
        return "register_page-success";
    }


//    -------------------------- Update Profil --------------------------


    @GetMapping(value = "/update-profile")
    public String getUpdateProfilePage(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount((Long) httpSession.getAttribute("idAccount"));
        accountDTO.setFullName(httpSession.getAttribute("fullName").toString());
        accountDTO.setEmail(httpSession.getAttribute("email").toString());
        model.addAttribute("account", accountDTO);

        try {
            Optional<ProjectDTO> projectDTO = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

            if (projectDTO.isPresent()) {
                projectService.deleteFileOrDirectory(projectDTO.get().getProjectPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectService.deleteDataInStaticObject();

        return "update_profile_page";
    }

    @GetMapping(value = "/update-profile-f")
    public String getUpdateProfilePageFailed(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount((Long) httpSession.getAttribute("idAccount"));
        accountDTO.setFullName(httpSession.getAttribute("fullName").toString());
        accountDTO.setEmail(httpSession.getAttribute("email").toString());

        model.addAttribute("account", accountDTO);
        model.addAttribute("message", "Email has been registered, use another email!");

        return "update_profile_page-show_message";
    }

    @GetMapping(value = "/update-profile-email-f")
    public String getUpdateProfilePageEmailFailed(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount((Long) httpSession.getAttribute("idAccount"));
        accountDTO.setFullName(httpSession.getAttribute("fullName").toString());
        accountDTO.setEmail(httpSession.getAttribute("email").toString());

        model.addAttribute("account", accountDTO);
        model.addAttribute("message", "Sorry, email message cannot be sent because the connection is not stable!");

        return "update_profile_page-show_message";
    }

    @PostMapping(value = "/update-profile-send-email")
    public String sendEmailInUpdateProfile(AccountDTO accountDTO, HttpSession httpSession){

        AccountDTO accountDTOSaved = accountService.updateAccount(accountDTO);

        return "redirect:update-profile-validate?id=" + accountDTOSaved.getIdAccount();

//        boolean isEmailSuccess = emailService.sendEmailUpdateValidation(accountDTOSaved);
//
//        if (isEmailSuccess) {
//            httpSession.invalidate();
//            return "redirect:/login";
//        } else {
//
//            if(accountDTOSaved == null){
//                return "redirect:/update-profile-f";
//            }
//
//            accountService.updateProfileBecauseEmailError(httpSession, accountDTOSaved);
//            return "redirect:/update-profile-email-f";
//        }

    }


    @GetMapping(value = "/update-profile-validate")
    public String updateAccount(@RequestParam long id){
        accountService.validateAccount(id);
        return "update_profile_page-success";
    }

}

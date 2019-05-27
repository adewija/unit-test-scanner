package com.awn.unittestscanner.controllers;

import com.awn.unittestscanner.dtos.ProjectDTO;
import com.awn.unittestscanner.dtos.RuleDTO;
import com.awn.unittestscanner.dtos.RuleGroupDTO;
import com.awn.unittestscanner.dtos.StaticDataProjectDTO;
import com.awn.unittestscanner.services.AccountService;
import com.awn.unittestscanner.services.ProjectService;
import com.awn.unittestscanner.services.RuleService;
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
public class RuleController {

    @Autowired
    RuleService ruleService;

    @Autowired
    AccountService accountService;

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/rule-list")
    public String getListRulePage(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        try {
            Optional<ProjectDTO> projectDTO = Optional.ofNullable(StaticDataProjectDTO.getProjectDTOHashMap().get(httpSession.getAttribute("email").toString()));

            if (projectDTO.isPresent()) {
                projectService.deleteFileOrDirectory(projectDTO.get().getProjectPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectService.deleteDataInStaticObject();

        List<RuleGroupDTO> ruleGroupDTOList = ruleService.getRuleByIdAccount((Long) httpSession.getAttribute("idAccount"));
        model.addAttribute("ruleGroupList", ruleGroupDTOList);

        return "list_of_rule_page";
    }


//    ============================= Rule Group ===============================

    @GetMapping(value = "/add-rule-group")
    public String getAddRuleGroupPage(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        model.addAttribute("ruleGroup", new RuleGroupDTO());

        return "add_rule_group_page";
    }

    @GetMapping(value = "/add-rule-group-f")
    public String getAddRuleGroupPageFailed(HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        model.addAttribute("ruleGroup", new RuleGroupDTO());

        return "add_rule_group_page-show_message";
    }

    @PostMapping(value = "/create-rule-group")
    public String createRuleGroup(HttpSession httpSession, RuleGroupDTO ruleGroupDTO){

        boolean isSuccess = ruleService.saveRuleGroup(ruleGroupDTO, (Long) httpSession.getAttribute("idAccount"));

        if (!isSuccess) {
            return "redirect:/add-rule-group-f";
        }

        return "redirect:/rule-list";
    }

    @GetMapping(value = "/edit-rule-group")
    public String getUpdateRuleGroupPage(@RequestParam long id, HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        RuleGroupDTO ruleGroupDTO = ruleService.getRuleGroup(id);

        model.addAttribute("ruleGroup", ruleGroupDTO);

        return "update_rule_group_page";
    }

    @GetMapping(value = "/edit-rule-group-f")
    public String getUpdateRuleGroupPageFailed(@RequestParam long id, HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        RuleGroupDTO ruleGroupDTO = ruleService.getRuleGroup(id);

        model.addAttribute("ruleGroup", ruleGroupDTO);

        return "update_rule_group_page-show_message";
    }

    @PostMapping(value = "/update-rule-group")
    public String updateRuleGroup(HttpSession httpSession, RuleGroupDTO ruleGroupDTO){

        boolean isSuccess = ruleService.updateRuleGroup(ruleGroupDTO, (Long) httpSession.getAttribute("idAccount"));

        if (!isSuccess) {
            return "redirect:/edit-rule-group-f?id="+ruleGroupDTO.getIdRuleGroup();
        }

        return "redirect:/rule-list";
    }

    @GetMapping(value = "/delete-rule-group")
    public String deleteRuleGroup(@RequestParam long id, HttpSession httpSession){

        ruleService.deleteRuleGroup(id);

        return "redirect:/rule-list";
    }




//    ============================= Rule ===============================


    @GetMapping(value = "/add-rule")
    public String getAddRulePage(@RequestParam long idRuleGroup, HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        model.addAttribute("idRuleGroup", idRuleGroup);
        model.addAttribute("rule", new RuleDTO());

        return "add_rule_page";
    }

    @PostMapping(value = "/create-rule")
    public String createRule(@RequestParam long idRuleGroup, HttpSession httpSession, RuleDTO ruleDTO){

        ruleService.saveRule(idRuleGroup, ruleDTO);

        return "redirect:/rule-list";
    }

    @GetMapping(value = "/edit-rule")
    public String getUpdateRulePage(@RequestParam long idRuleGroup, @RequestParam long idRule, HttpSession httpSession, Model model){

        if (!accountService.isAlreadyLoggedIn(httpSession)){
            return "redirect:/login-ia";
        }

        RuleGroupDTO ruleGroupDTO = ruleService.getRuleGroup(idRuleGroup);
        RuleDTO ruleDTO = ruleService.getRule(idRule);

        model.addAttribute("rule", ruleDTO);
        model.addAttribute("idRuleGroup", ruleGroupDTO.getIdRuleGroup());

        return "update_rule_page";
    }

    @PostMapping(value = "/update-rule")
    public String updateRule(@RequestParam long idRuleGroup, HttpSession httpSession, RuleDTO ruleDTO){

        RuleGroupDTO ruleGroupDTO = ruleService.getRuleGroup(idRuleGroup);

        ruleDTO.setRuleGroup(ruleGroupDTO);

        ruleService.updateRule(ruleDTO);

        return "redirect:/rule-list";
    }

    @GetMapping(value = "/delete-rule")
    public String deleteRule(@RequestParam long id, HttpSession httpSession){

        ruleService.deleteRule(id);

        return "redirect:/rule-list";
    }

}
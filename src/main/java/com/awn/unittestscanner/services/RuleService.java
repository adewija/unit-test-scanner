package com.awn.unittestscanner.services;

import com.awn.unittestscanner.dtos.RuleDTO;
import com.awn.unittestscanner.dtos.RuleGroupDTO;
import com.awn.unittestscanner.entities.Account;
import com.awn.unittestscanner.entities.Rule;
import com.awn.unittestscanner.entities.RuleGroup;
import com.awn.unittestscanner.repositories.AccountRepository;
import com.awn.unittestscanner.repositories.RuleGroupRepository;
import com.awn.unittestscanner.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RuleService {

    @Autowired
    RuleGroupRepository ruleGroupRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<RuleGroupDTO> getRuleByIdAccount(long idAccount) {
        Optional<List<RuleGroup>> optionalRuleGroupList = ruleGroupRepository.findAllByAccount_IdAccount(idAccount);
        List<RuleGroupDTO> ruleGroupDTOList = new ArrayList<>();

        if (optionalRuleGroupList.isPresent()) {
            ruleGroupDTOList = convertToRuleGroupDTOList(optionalRuleGroupList.get());
        }
        return ruleGroupDTOList;
    }

    public boolean saveRuleGroup(RuleGroupDTO ruleGroupDTO, long idAccount) {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);

        if (optionalAccount.isPresent()) {
            Optional<RuleGroup> optionalRuleGroup = ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), optionalAccount.get().getIdAccount());

            if (optionalRuleGroup.isPresent()) {

                return false;

            } else {
                RuleGroup ruleGroup = new RuleGroup();
                ruleGroup.setRuleGroupName(ruleGroupDTO.getRuleGroupName());
                ruleGroup.setRuleGroupDescription(ruleGroupDTO.getRuleGroupDescription());
                ruleGroup.setAccount(optionalAccount.get());
                ruleGroupRepository.save(ruleGroup);

                return true;
            }
        }
        return false;

    }

    public boolean updateRuleGroup(RuleGroupDTO ruleGroupDTO, long idAccount) {
        boolean isUpdated = false;

        Optional<Account> optionalAccount = accountRepository.findById(idAccount);

        if (optionalAccount.isPresent()) {
            Optional<RuleGroup> optionalRuleGroup = ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), optionalAccount.get().getIdAccount());

            if (optionalRuleGroup.isPresent()) {

                if (optionalRuleGroup.get().getIdRuleGroup() == ruleGroupDTO.getIdRuleGroup()) {
                    RuleGroup ruleGroup = new RuleGroup();
                    ruleGroup.setIdRuleGroup(ruleGroupDTO.getIdRuleGroup());
                    ruleGroup.setRuleGroupName(ruleGroupDTO.getRuleGroupName());
                    ruleGroup.setRuleGroupDescription(ruleGroupDTO.getRuleGroupDescription());
                    ruleGroup.setAccount(optionalAccount.get());
                    ruleGroupRepository.save(ruleGroup);
                    isUpdated = true;
                }
            } else {

                RuleGroup ruleGroup = new RuleGroup();
                ruleGroup.setIdRuleGroup(ruleGroupDTO.getIdRuleGroup());
                ruleGroup.setRuleGroupName(ruleGroupDTO.getRuleGroupName());
                ruleGroup.setRuleGroupDescription(ruleGroupDTO.getRuleGroupDescription());
                ruleGroup.setAccount(optionalAccount.get());
                ruleGroupRepository.save(ruleGroup);
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    public RuleGroupDTO getRuleGroup(Long id) {
        Optional<RuleGroup> optionalRuleGroup = ruleGroupRepository.findById(id);

        if (optionalRuleGroup.isPresent()) {
            RuleGroup ruleGroup = optionalRuleGroup.get();
            RuleGroupDTO ruleGroupDTO = convertToRuleGroupDTO(ruleGroup);
            return ruleGroupDTO;
        } else {
            return null;
        }
    }

    public void deleteRuleGroup(Long id) {
        ruleRepository.deleteAllByRuleGroupIdRuleGroup(id);
        ruleGroupRepository.deleteById(id);

    }

    private RuleGroupDTO convertToRuleGroupDTO(RuleGroup ruleGroup) {
        RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
        ruleGroupDTO.setIdRuleGroup(ruleGroup.getIdRuleGroup());
        ruleGroupDTO.setRuleGroupName(ruleGroup.getRuleGroupName());
        ruleGroupDTO.setRuleGroupDescription(ruleGroup.getRuleGroupDescription());
        return ruleGroupDTO;
    }


    private List<RuleGroupDTO> convertToRuleGroupDTOList(List<RuleGroup> ruleGroupList) {
        List<RuleGroupDTO> ruleGroupDTOList = new ArrayList<>();

        for (RuleGroup ruleGroup : ruleGroupList) {
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
            ruleGroupDTOList.add(ruleGroupDTO);
        }

        return ruleGroupDTOList;
    }

//    ======================================================================================

    public void saveRule(long idRuleGroup, RuleDTO ruleDTO) {
        RuleGroup ruleGroup = ruleGroupRepository.getOne(idRuleGroup);

        Rule rule = new Rule();
        rule.setIdRule(ruleDTO.getIdRule());
        rule.setRuleName(ruleDTO.getRuleName());
        rule.setRuleDescription(ruleDTO.getRuleDescription());
        rule.setRuleSyntax(ruleDTO.getRuleSyntax());
        rule.setRuleGroup(ruleGroup);

        ruleRepository.save(rule);
    }

    public void updateRule(RuleDTO ruleDTO) {

        Rule rule = new Rule();
        rule.setIdRule(ruleDTO.getIdRule());
        rule.setRuleName(ruleDTO.getRuleName());
        rule.setRuleDescription(ruleDTO.getRuleDescription());
        rule.setRuleSyntax(ruleDTO.getRuleSyntax());

        RuleGroupDTO ruleGroupDTO = ruleDTO.getRuleGroup();
        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setIdRuleGroup(ruleGroupDTO.getIdRuleGroup());
        ruleGroup.setRuleGroupName(ruleGroupDTO.getRuleGroupName());
        ruleGroup.setRuleGroupDescription(ruleGroupDTO.getRuleGroupDescription());

        rule.setRuleGroup(ruleGroup);

        ruleRepository.save(rule);
    }

    public RuleDTO getRule(Long id) {
        Optional<Rule> optionalRule = ruleRepository.findById(id);

        if (optionalRule.isPresent()) {
            Rule rule = optionalRule.get();
            RuleDTO ruleDTO = convertToRuleDTO(rule);
            return ruleDTO;
        } else {
            return null;
        }
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    private RuleDTO convertToRuleDTO(Rule rule) {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.setIdRule(rule.getIdRule());
        ruleDTO.setRuleName(rule.getRuleName());
        ruleDTO.setRuleDescription(rule.getRuleDescription());
        ruleDTO.setRuleSyntax(rule.getRuleSyntax());
        return ruleDTO;
    }

}

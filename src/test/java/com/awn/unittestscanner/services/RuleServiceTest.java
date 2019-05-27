package com.awn.unittestscanner.services;

import com.awn.unittestscanner.dtos.RuleGroupDTO;
import com.awn.unittestscanner.entities.Account;
import com.awn.unittestscanner.entities.RuleGroup;
import com.awn.unittestscanner.repositories.AccountRepository;
import com.awn.unittestscanner.repositories.RuleGroupRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RuleServiceTest {
    @InjectMocks
    RuleService ruleService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    RuleGroupRepository ruleGroupRepository;

    @Before
    public void setUp(){
        initMocks(this);
    }

    @After
    public void tearDown(){
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(ruleGroupRepository);
    }

    @Test
    public void updateRuleGroupPath1Test(){
        long idAccount = 111;
        RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
        Optional<Account> accountOptional = Optional.ofNullable(null);

        when(accountRepository.findById(idAccount)).thenReturn(accountOptional);

        boolean result = ruleService.updateRuleGroup(ruleGroupDTO, idAccount);

        verify(accountRepository).findById(idAccount);

        assertFalse(result);
    }

    @Test
    public void updateRuleGroupPath2Test(){
        long idAccount = 111;
        RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
        ruleGroupDTO.setRuleGroupName("Dummy");

        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setRuleGroupName("Dummy");

        Account account = new Account();
        account.setIdAccount(111);

        Optional<Account> accountOptional = Optional.of(account);

        Optional<RuleGroup> ruleGroupOptional = Optional.ofNullable(null);

        when(accountRepository.findById(idAccount)).thenReturn(accountOptional);
        when(ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount)).thenReturn(ruleGroupOptional);

        when(ruleGroupRepository.save(ruleGroup)).thenReturn(ruleGroup);

        boolean result = ruleService.updateRuleGroup(ruleGroupDTO, idAccount);

        verify(accountRepository).findById(idAccount);
        verify(ruleGroupRepository).findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount);
        verify(ruleGroupRepository).save(any(RuleGroup.class));

        assertTrue(result);
    }

    @Test
    public void updateRuleGroupPath3Test(){
        long idAccount = 111;
        RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
        ruleGroupDTO.setRuleGroupName("Dummy");
        ruleGroupDTO.setIdRuleGroup(111);

        Account account = new Account();
        account.setIdAccount(111);

        Optional<Account> accountOptional = Optional.of(account);

        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setIdRuleGroup(222);

        Optional<RuleGroup> ruleGroupOptional = Optional.of(ruleGroup);

        when(accountRepository.findById(idAccount)).thenReturn(accountOptional);
        when(ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount)).thenReturn(ruleGroupOptional);

        boolean result = ruleService.updateRuleGroup(ruleGroupDTO, idAccount);

        verify(accountRepository).findById(idAccount);
        verify(ruleGroupRepository).findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount);

        assertFalse(result);
    }

    @Test
    public void updateRuleGroupPath4Test(){
        long idAccount = 111;
        RuleGroupDTO ruleGroupDTO = new RuleGroupDTO();
        ruleGroupDTO.setRuleGroupName("Dummy");
        ruleGroupDTO.setIdRuleGroup(111);

        Account account = new Account();
        account.setIdAccount(111);

        Optional<Account> accountOptional = Optional.of(account);

        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setIdRuleGroup(111);

        Optional<RuleGroup> ruleGroupOptional = Optional.of(ruleGroup);

        when(accountRepository.findById(idAccount)).thenReturn(accountOptional);
        when(ruleGroupRepository.findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount)).thenReturn(ruleGroupOptional);
        when(ruleGroupRepository.save(ruleGroup)).thenReturn(ruleGroup);

        boolean result = ruleService.updateRuleGroup(ruleGroupDTO, idAccount);

        verify(accountRepository).findById(idAccount);
        verify(ruleGroupRepository).findRuleGroupByRuleGroupNameAndAccount_IdAccount(ruleGroupDTO.getRuleGroupName(), idAccount);
        verify(ruleGroupRepository).save(any(RuleGroup.class));

        assertTrue(result);
    }
}

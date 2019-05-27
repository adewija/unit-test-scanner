package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.RuleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleGroupRepository extends JpaRepository<RuleGroup, Long> {

    @Override
    RuleGroup getOne(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<RuleGroup> findById(Long aLong);

    @Override
    <S extends RuleGroup> S save(S s);

    Optional<RuleGroup> findRuleGroupByRuleGroupNameAndAccount_IdAccount(String ruleGroupName, long idAccount);

    Optional<List<RuleGroup>> findAllByAccount_IdAccount(long idAccount);

}

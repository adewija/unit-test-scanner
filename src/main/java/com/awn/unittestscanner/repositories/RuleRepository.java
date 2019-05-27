package com.awn.unittestscanner.repositories;

import com.awn.unittestscanner.entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    @Override
    <S extends Rule> S save(S s);

    @Override
    Optional<Rule> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    void deleteAllByRuleGroupIdRuleGroup(long idRuleGroup);

}

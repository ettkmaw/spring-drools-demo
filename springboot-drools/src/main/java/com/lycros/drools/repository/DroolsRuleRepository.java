package com.lycros.drools.repository;

import com.lycros.drools.domain.DroolsRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DroolsRuleRepository extends JpaRepository<DroolsRule, Long> {

    long deleteByRuleId(Long ruleId);

    @Transactional
    @Modifying
    @Query("update DroolsRule d set d.kieBaseName = ?1, d.kiePackageName = ?2, d.ruleContent = ?3 " +
            "where d.kieBaseName = ?4")
    int updateRule(String kieBaseName, String kiePackageName, String ruleContent, String kieBaseName1);
    Optional<DroolsRule> findByRuleId(Long ruleId);

}
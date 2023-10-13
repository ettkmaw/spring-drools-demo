package com.lycros.drools.server;

import com.lycros.drools.domain.DroolsRule;

import java.util.List;

public interface DroolsRuleService {
    List<DroolsRule> findAll();

    void addDroolsRule(DroolsRule droolsRule);

    void updateDroolsRule(DroolsRule droolsRule);

    void deleteDroolsRule(Long ruleId, String ruleName);
}

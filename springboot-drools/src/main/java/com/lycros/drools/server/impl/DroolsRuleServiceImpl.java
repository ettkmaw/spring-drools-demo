package com.lycros.drools.server.impl;
import com.lycros.drools.config.DroolsManager;
import com.lycros.drools.domain.DroolsRule;
import com.lycros.drools.server.DroolsRuleService;
import org.springframework.stereotype.Service;
 
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DroolsRuleServiceImpl implements DroolsRuleService {
 
    @Resource
    private DroolsManager droolsManager;

    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);
 
    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }
 
    @Override
    public void addDroolsRule(DroolsRule droolsRule) {

        droolsRule.setCreatedTime(LocalDateTime.now());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }
 
    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {

        droolsRule.setUpdateTime(LocalDateTime.now());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }
 
    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule.getKieBaseName(), droolsRule.getKiePackageName(), ruleName);
        }
    }
}
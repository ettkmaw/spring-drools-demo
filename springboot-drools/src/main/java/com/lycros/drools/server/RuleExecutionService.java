package com.lycros.drools.server;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleExecutionService {
    private final KieContainer kieContainer;

    @Autowired
    public RuleExecutionService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void executeRules(Object factObject) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(factObject); // 插入事实对象
            kieSession.fireAllRules(); // 执行规则
        } finally {
            kieSession.dispose();
        }
    }
}

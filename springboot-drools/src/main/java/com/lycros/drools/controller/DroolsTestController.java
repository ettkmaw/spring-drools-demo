package com.lycros.drools.controller;

import com.lycros.drools.domain.DroolsRule;
import com.lycros.drools.domain.Message;
import com.lycros.drools.repository.DroolsRuleRepository;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.*;

/**
 * @author dj
 * @date 2023/10/1315:41
 * @description TODO
 */
@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class DroolsTestController {

    private final KieContainer kieContainer;

    private final DroolsRuleRepository ruleRepository;


    @RequestMapping("/do")
    public String ruleDone(@RequestParam String kieBaseName,@RequestParam String param){
        KieSession kieSession = kieContainer.newKieSession(kieBaseName + "-session");;
        Message message = new Message();
        message.setMessage(param);
        message.setStatus( Message.HELLO );
        kieSession.insert( message );
        // Fire the rules.
        kieSession.fireAllRules();
        return message.getMessage();
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Long ruleAdd(@RequestBody DroolsRule droolsRule){
        DroolsRule save = ruleRepository.save(droolsRule);
        return save.getRuleId();
    }
}

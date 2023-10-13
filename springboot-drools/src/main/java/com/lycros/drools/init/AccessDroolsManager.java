package com.lycros.drools.init;

import com.lycros.drools.config.DroolsManager;
import com.lycros.drools.domain.DroolsRule;
import com.lycros.drools.repository.DroolsRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dj
 * @date 2023/10/1314:26
 * @description TODO
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDroolsManager  implements CommandLineRunner {

    private final DroolsRuleRepository droolsRuleRepository;

    private final DroolsManager droolsManager;



    @Override
    public void run(String... args) {
        System.out.println("开始从数据库读取规则并加载......");
        List<DroolsRule> ruleList = droolsRuleRepository.findAll();
        ruleList.forEach(droolsManager::addOrUpdateRule);
        System.out.println(String.format("数据库规则加载完毕，共加载%d条规则......",ruleList.size()));
    }
}

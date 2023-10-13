package com.lycros.drools.config;

import com.lycros.drools.domain.DroolsRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroolsManager {

    // 此类本身就是单例的
    private final KieServices kieServices=KieServices.Factory.get();
    // kie文件系统，需要缓存，如果每次添加规则都是重新new一个的话，则可能出现问题。即之前加到文件系统中的规则没有了
    private final KieFileSystem kieFileSystem;
    // 构建 kmodule.xml
    private final KieModuleModel kieModuleModel;
    // 需要全局唯一
    private final   KieContainer kieContainer;

    /**
     * 判断该kbase是否存在
     */
    public boolean existsKieBase(String kieBaseName) {
        if (null == kieContainer) {
            return false;
        }
        Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        if (kieBaseNames.contains(kieBaseName)) {
            return true;
        }
        log.info("需要创建KieBase:{}", kieBaseName);
        return false;
    }

    public void deleteDroolsRule(String kieBaseName, String packageName, String ruleName) {
        if (existsKieBase(kieBaseName)) {
            KieBase kieBase = kieContainer.getKieBase(kieBaseName);
            kieBase.removeRule(packageName, ruleName);
            log.info("删除kieBase:[{}]包:[{}]下的规则:[{}]", kieBaseName, packageName, ruleName);
        }
    }

    /**
     * 添加或更新 drools 规则
     */
    public void addOrUpdateRule(DroolsRule droolsRule) {
        // 获取kbase的名称
        String kieBaseName = droolsRule.getKieBaseName();
        // 判断该kbase是否存在
        boolean existsKieBase = existsKieBase(kieBaseName);
        // 该对象对应kmodule.xml中的kbase标签
        KieBaseModel kieBaseModel = null;
        if (!existsKieBase) {
            // 创建一个kbase
            kieBaseModel = kieModuleModel.newKieBaseModel(kieBaseName);
            // 不是默认的kieBase
            kieBaseModel.setDefault(false);
            // 设置该KieBase需要加载的包路径
            kieBaseModel.addPackage(droolsRule.getKiePackageName());
            // 设置kieSession
            kieBaseModel.newKieSessionModel(kieBaseName + "-session")
                    // 不是默认session
                    .setDefault(false);
        } else {
            // 获取到已经存在的kbase对象
            kieBaseModel = kieModuleModel.getKieBaseModels().get(kieBaseName);
            // 获取到packages
            List<String> packages = kieBaseModel.getPackages();
            if (!packages.contains(droolsRule.getKiePackageName())) {
                kieBaseModel.addPackage(droolsRule.getKiePackageName());
                log.info("kieBase:{}添加一个新的包:{}", kieBaseName, droolsRule.getKiePackageName());
            } else {
                kieBaseModel = null;
            }
        }
        String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
        log.info("加载虚拟规则文件:{}", file);
        kieFileSystem.write(file, droolsRule.getRuleContent());
        if (kieBaseModel != null) {
            String kmoduleXml = kieModuleModel.toXML();
            log.info("加载kmodule.xml:[\n{}]", kmoduleXml);
            kieFileSystem.writeKModuleXML(kmoduleXml);
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        // 通过KieBuilder构建KieModule下所有的KieBase
        kieBuilder.buildAll();
        // 获取构建过程中的结果
        Results results = kieBuilder.getResults();
        // 获取错误信息
        List<Message> messages = results.getMessages(Message.Level.ERROR);
        if (null != messages && !messages.isEmpty()) {
            for (Message message : messages) {
                log.error(message.getText());
            }
            throw new RuntimeException("加载规则出现异常");
        }
        // 实现动态更新
        ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());

    }
}
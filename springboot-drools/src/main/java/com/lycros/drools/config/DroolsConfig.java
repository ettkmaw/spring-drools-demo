package com.lycros.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    public KieServices getKieServices(){
        return KieServices.Factory.get();
    }


    @Bean
    @ConditionalOnMissingBean(KieFileSystem.class)
    public KieFileSystem kieFileSystem() {
        return  getKieServices().newKieFileSystem();
    }

    @Bean
    public KieModuleModel kieModuleModel(){
       return getKieServices().newKieModuleModel();
    }

    @Bean
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer() {
        KieRepository kieRepository = getKieServices().getRepository();
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm");
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
    }
}

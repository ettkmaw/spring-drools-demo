package com.lycros.drools;

import com.lycros.drools.domain.Message;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.builder.model.ListenerModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * @author dj
 * @date 2023/10/1215:27
 * @description TODO
 */
public class DemoTest {
    public static void main(String[] args) {
//        KieServices ks = KieServices.Factory.get();
//        KieModuleModel km = ks.newKieModuleModel();
//
//        KieBaseModel kbm=km.newKieBaseModel("kieBase01");
//        kbm.setDefault(false);
//        kbm.addPackage("rules/rule01");
//        KieSessionModel ksm=kbm.newKieSessionModel("kieBase01-session");
//
//        ksm.newListenerModel("org.kie.api.event.rule.DebugAgendaEventListener", ListenerModel.Kind.AGENDA_EVENT_LISTENER);
//
//        System.out.println(km.toXML());
//        KieFileSystem kieFileSystem = ks.newKieFileSystem();
//        kieFileSystem.writeKModuleXML(km.toXML());
//        kieFileSystem.write("src/main/resources/rules/1.drl","drl规则内容");
//        KieBuilder kieBuilder = ks.newKieBuilder(kieFileSystem);
//        kieBuilder.buildAll();
//        Results results = kieBuilder.getResults();
//        List<org.kie.api.builder.Message> messages = results.getMessages(org.kie.api.builder.Message.Level.ERROR);
//        if(null != messages && !messages.isEmpty()){
//            for (org.kie.api.builder.Message message : messages) {
//                System.out.println("msg>>>>>"+message);
//            }
//        }



        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        KieSession ksession = kc.newKieSession("HelloWorldKS");
        final Message message = new Message();
        message.setMessage( "Hello World" );
        message.setStatus( Message.HELLO );
        ksession.insert( message );
        // Fire the rules.
        ksession.fireAllRules();
        kc.dispose();
    }
}

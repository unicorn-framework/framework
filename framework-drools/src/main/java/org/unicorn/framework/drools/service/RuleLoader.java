package org.unicorn.framework.drools.service;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.unicorn.framework.drools.bo.RuleInfo;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 规则加载器
 *
 * @author xiebin
 */
@Component
public class RuleLoader implements ApplicationRunner {

    /**
     * key:kcontainerName,value:KieContainer，每个场景对应一个KieContainer
     */
    private final ConcurrentMap<String, KieContainer> kieContainerMap = new ConcurrentHashMap<>();

    @Autowired
    private RuleInfoService ruleInfoService;

    @Autowired
    private KieServices kieServices;

    @Autowired
    private KieFileSystem kieFileSystem;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        reloadAll();
    }

    /**
     * 构造kcontainerName
     *
     * @param sceneKey 场景ID
     * @return kcontainerName
     */
    private String buildKcontainerName(String sceneKey) {
        return "kcontainer_" + sceneKey;
    }

    /**
     * 构造kbaseName
     *
     * @param sceneKey 场景ID
     * @return kbaseName
     */
    private String buildKbaseName(String sceneKey) {
        return "kbase_" + sceneKey;
    }

    /**
     * 构造ksessionName
     *
     * @param sceneKey 场景ID
     * @return ksessionName
     */
    private String buildKsessionName(String sceneKey) {
        return "ksession_" + sceneKey;
    }

    public KieContainer getKieContainerBySceneId(String sceneKey) {
        return kieContainerMap.get(buildKcontainerName(sceneKey));
    }

    /**
     * 重新加载所有规则
     */
    public void reloadAll() {
        Map<String, List<RuleInfo>> sceneId2RuleInfoListMap = ruleInfoService.getRuleInfoListMap();
        for (Map.Entry<String, List<RuleInfo>> entry : sceneId2RuleInfoListMap.entrySet()) {
            String sceneKey = entry.getKey();
            reload(sceneKey, entry.getValue());
        }
        System.out.println("reload all success");
    }

    /**
     * 重新加载给定场景下的规则
     *
     * @param sceneKey 场景ID
     */
    public void reload(String sceneKey) {
        List<RuleInfo> ruleInfos = ruleInfoService.getRuleInfoListBySceneId(sceneKey);
        reload(sceneKey, ruleInfos);
        System.out.println("reload success");
    }

    /**
     * 重新加载给定场景给定规则列表，对应一个kmodule
     *
     * @param sceneKey   场景ID
     * @param ruleInfos 规则列表
     */
    private void reload(String sceneKey, List<RuleInfo> ruleInfos) {

        for (RuleInfo ruleInfo : ruleInfos) {
            String fullPath = MessageFormat.format("src/main/resources/rules/scene_{0}/rule_{1}.drl", String.valueOf(sceneKey), String.valueOf(ruleInfo.getId()));
            kieFileSystem.write(fullPath, ruleInfo.getContent());
        }
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("rule error");
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        kieContainerMap.put(buildKcontainerName(sceneKey), kieContainer);
    }
}

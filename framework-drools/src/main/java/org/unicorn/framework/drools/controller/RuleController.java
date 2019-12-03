package org.unicorn.framework.drools.controller;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicorn.framework.drools.service.RuleLoader;

/**
 * 规则测试
 *
 * @author xiebin
 */
@RequestMapping("rule")
@RestController
@Slf4j
public class RuleController {

    @Autowired
    private RuleLoader ruleLoader;


    /**
     * 重新加载所有规则
     */
    @GetMapping("reload")
    public String reload() {
        log.info("reload all");
        ruleLoader.reloadAll();
        return "success";
    }

    /**
     * 重新加载给定场景下的规则
     *
     * @param sceneKey 场景ID
     */
    @GetMapping("reload/{sceneId}")
    public String reload(@PathVariable("sceneId") String sceneKey) {
        log.info("reload scene:" + sceneKey);
        ruleLoader.reload(sceneKey);
        return "success";
    }

    /**
     * 触发给定场景规则
     *
     * @param sceneKey 场景ID
     */
    @GetMapping("fire/{sceneId}")
    public String fire(@PathVariable("sceneId") String sceneKey) {
        log.info("fire scene:" + sceneKey);
        KieSession kieSession = ruleLoader.getKieContainerBySceneId(sceneKey).getKieBase().newKieSession();
        kieSession.fireAllRules();
        kieSession.dispose();
        return "success";
    }

}

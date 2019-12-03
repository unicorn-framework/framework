package org.unicorn.framework.drools.service;

import org.unicorn.framework.drools.bo.RuleInfo;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 规则信息业务
 *
 * @author xiebin
 */
public abstract class RuleInfoService {

    /**
     * 获取给定场景下的规则信息列表
     *
     * @param sceneKey 场景ID
     * @return 规则列表
     */
    public List<RuleInfo> getRuleInfoListBySceneId(String sceneKey) {
        Map<String, List<RuleInfo>> sceneId2RuleInfoListMap = getRuleInfoListMap();
        return sceneId2RuleInfoListMap.get(sceneKey);
    }

    /**
     * 获取场景与规则信息列表的Map
     *
     * @return 场景规则信息列表Map，Map(sceneId : List < RuleInfo >)
     */
    public Map<String, List<RuleInfo>> getRuleInfoListMap() {
        Map<String, List<RuleInfo>> sceneId2RuleInfoListMap = new HashMap<>();
        List<RuleInfo> allRuleInfos = generateRuleInfoList();
        for (RuleInfo ruleInfo : allRuleInfos) {
            List<RuleInfo> ruleInfos = sceneId2RuleInfoListMap.computeIfAbsent(ruleInfo.getSceneKey(), k -> new ArrayList<>());
            ruleInfos.add(ruleInfo);
        }
        return sceneId2RuleInfoListMap;
    }

    /**
     * 生成规则信息列表，注意场景id和规则id的对应关系
     *
     * @return 规则信息列表
     */
    public abstract List<RuleInfo> generateRuleInfoList();


    /**
     * 生成规则信息
     *
     * @param sceneKey 场景ID
     * @param id       规则ID
     * @return 规则信息
     */
    private RuleInfo generateRuleInfo(String sceneKey, long id) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setId(id);
        ruleInfo.setSceneKey(sceneKey);
        ruleInfo.setContent(generateRuleContent(sceneKey, id));
        return ruleInfo;
    }

    /**
     * 生成规则内容，每个场景id对应一个package，每个规则对应一个唯一的规则名
     * <p>
     * 每次生成规则时记录时间戳，用来验证动态加载效果
     *
     * @param sceneId 场景ID
     * @param id      规则ID
     * @return 规则内容
     */
    private String generateRuleContent(String sceneKey, long id) {
        String sceneIdStr = String.valueOf(sceneKey);
        String idStr = String.valueOf(id);
        String nowStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String content = "package rules.scene_{0};\n" +
                "\n" +
                "rule \"rule_{1}\"\n" +
                "    when\n" +
                "        eval(true);\n" +
                "    then\n" +
                "        System.out.println(\"{2} [{3}, {4}]\");\n" +
                "end\n";
        return MessageFormat.format(content, sceneIdStr, idStr, nowStr, sceneIdStr, idStr);
    }
}

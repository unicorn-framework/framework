package org.unicorn.framework.elastic.parser;

import org.springframework.util.StringUtils;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.base.JobAttributeTag;
import org.unicorn.framework.elastic.dynamic.bean.Job;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 9:51
 */
public abstract class AbstractParserJob implements IParserJob {

    private String prefix = "unicorn.elastic.job.";

    @Override
    public Job parserJob(Object bean) {
        return parserJobFromJobType(bean);
    }

    public abstract Job parserJobFromJobType(Object bean);

    /**
     * 构造Job对象
     *
     * @param confBean
     * @return
     */
    public Job setJobCommonProperties(Job job, Object confBean) {
        Class<?> clz = confBean.getClass();
        String jobClass = clz.getName();

        ElasticJobConf conf = clz.getAnnotation(ElasticJobConf.class);
        //job名称
        String jobName = conf.name();
        String cron = getEnvironmentStringValue(jobName, JobAttributeTag.CRON, conf.cron());
        String shardingItemParameters = getEnvironmentStringValue(jobName, JobAttributeTag.SHARDING_ITEM_PARAMETERS, conf.shardingItemParameters());
        String description = getEnvironmentStringValue(jobName, JobAttributeTag.DESCRIPTION, conf.description());
        String jobParameter = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_PARAMETER, conf.jobParameter());
        String jobExceptionHandler = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_EXCEPTION_HANDLER, conf.jobExceptionHandler());
        String executorServiceHandler = getEnvironmentStringValue(jobName, JobAttributeTag.EXECUTOR_SERVICE_HANDLER, conf.executorServiceHandler());

        String jobShardingStrategyClass = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_SHARDING_STRATEGY_CLASS, conf.jobShardingStrategyClass());
        String eventTraceRdbDataSource = getEnvironmentStringValue(jobName, JobAttributeTag.EVENT_TRACE_RDB_DATA_SOURCE, conf.eventTraceRdbDataSource());
        String scriptCommandLine = getEnvironmentStringValue(jobName, JobAttributeTag.SCRIPT_COMMAND_LINE, conf.scriptCommandLine());

        boolean failover = getEnvironmentBooleanValue(jobName, JobAttributeTag.FAILOVER, conf.failover());
        boolean misfire = getEnvironmentBooleanValue(jobName, JobAttributeTag.MISFIRE, conf.misfire());
        boolean overwrite = getEnvironmentBooleanValue(jobName, JobAttributeTag.OVERWRITE, conf.overwrite());
        boolean disabled = getEnvironmentBooleanValue(jobName, JobAttributeTag.DISABLED, conf.disabled());
        boolean monitorExecution = getEnvironmentBooleanValue(jobName, JobAttributeTag.MONITOR_EXECUTION, conf.monitorExecution());
        boolean streamingProcess = getEnvironmentBooleanValue(jobName, JobAttributeTag.STREAMING_PROCESS, conf.streamingProcess());
        boolean once = getEnvironmentBooleanValue(jobName, JobAttributeTag.ONCE, conf.once());

        int shardingTotalCount = getEnvironmentIntValue(jobName, JobAttributeTag.SHARDING_TOTAL_COUNT, conf.shardingTotalCount());
        int monitorPort = getEnvironmentIntValue(jobName, JobAttributeTag.MONITOR_PORT, conf.monitorPort());
        int maxTimeDiffSeconds = getEnvironmentIntValue(jobName, JobAttributeTag.MAX_TIME_DIFF_SECONDS, conf.maxTimeDiffSeconds());
        int reconcileIntervalMinutes = getEnvironmentIntValue(jobName, JobAttributeTag.RECONCILE_INTERVAL_MINUTES, conf.reconcileIntervalMinutes());

        job.setJobName(jobName);
        job.setJobType(conf.jobType().toString());
        job.setJobClass(jobClass);
        job.setCron(cron);
        job.setShardingItemParameters(shardingItemParameters);
        job.setDescription(description);
        job.setJobParameter(jobParameter);
        job.getJobProperties().setJobExceptionHandler(jobExceptionHandler);
        job.getJobProperties().setExecutorServiceHandler(executorServiceHandler);
        job.setJobShardingStrategyClass(jobShardingStrategyClass);
        job.setEventTraceRdbDataSource(eventTraceRdbDataSource);
        job.setScriptCommandLine(scriptCommandLine);
        job.setFailover(failover);
        job.setMisfire(misfire);
        job.setOnce(once);
        job.setOverwrite(overwrite);
        job.setDisabled(disabled);
        job.setMonitorExecution(monitorExecution);
        job.setStreamingProcess(streamingProcess);
        job.setShardingTotalCount(shardingTotalCount);
        job.setMonitorPort(monitorPort);
        job.setMaxTimeDiffSeconds(maxTimeDiffSeconds);
        job.setReconcileIntervalMinutes(reconcileIntervalMinutes);
        return job;
    }


    /**
     * 获取配置中的任务属性值，environment没有就用注解中的值
     *
     * @param jobName      任务名称
     * @param fieldName    属性名称
     * @param defaultValue 默认值
     * @return
     */
    public String getEnvironmentStringValue(String jobName, String fieldName, String defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(key);
        if (StringUtils.hasText(value)) {
            return value;
        }
        return defaultValue;
    }

    public int getEnvironmentIntValue(String jobName, String fieldName, int defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(key);
        if (StringUtils.hasText(value)) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }


    public boolean getEnvironmentBooleanValue(String jobName, String fieldName, boolean defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(key);
        if (StringUtils.hasText(value)) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }


}

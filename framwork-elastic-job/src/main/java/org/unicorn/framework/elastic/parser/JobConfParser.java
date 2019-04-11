package org.unicorn.framework.elastic.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.base.JobAttributeTag;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.service.JobService;

import java.util.Map;

/**
 * Job解析类
 * 
 * <p>从注解中解析任务信息初始化<p>
 * 
 * @author xiebin
 *
 */
public class JobConfParser  implements ApplicationContextAware {
	
	private Logger logger = LoggerFactory.getLogger(JobConfParser.class);

	private String prefix = "unicorn.elastic.job.";
	
	private Environment environment;
	
	@Autowired(required=false)
	private JobService jobService;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		environment = ctx.getEnvironment();
		//获取ElasticJobConf注解的类
		Map<String, Object> beanMap = ctx.getBeansWithAnnotation(ElasticJobConf.class);
		for (Object confBean : beanMap.values()) {
			Job job=job(confBean);
			jobService.addJob(job);
		}
		//开启任务监听,当有任务添加时，监听zk中的数据增加，自动在其他节点也初始化该任务
		if (jobService != null) {
			jobService.monitorJobRegister();
		}
		
	}

	/**
	 * 构造Job对象
	 * @param confBean
	 * @return
	 */
	private Job job(Object confBean){
		Class<?> clz = confBean.getClass();
		String jobTypeName = confBean.getClass().getInterfaces()[0].getSimpleName();
		ElasticJobConf conf = clz.getAnnotation(ElasticJobConf.class);

		String jobClass = clz.getName();
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

		int shardingTotalCount = getEnvironmentIntValue(jobName, JobAttributeTag.SHARDING_TOTAL_COUNT, conf.shardingTotalCount());
		int monitorPort = getEnvironmentIntValue(jobName, JobAttributeTag.MONITOR_PORT, conf.monitorPort());
		int maxTimeDiffSeconds = getEnvironmentIntValue(jobName, JobAttributeTag.MAX_TIME_DIFF_SECONDS, conf.maxTimeDiffSeconds());
		int reconcileIntervalMinutes = getEnvironmentIntValue(jobName, JobAttributeTag.RECONCILE_INTERVAL_MINUTES, conf.reconcileIntervalMinutes());

		Job job=new Job();
		job.setJobType(jobTypeName);
		job.setJobClass(jobClass);
		job.setJobName(jobName);
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
	 * @param jobName		任务名称
	 * @param fieldName		属性名称
	 * @param defaultValue  默认值
	 * @return
	 */
	private String getEnvironmentStringValue(String jobName, String fieldName, String defaultValue) {
		String key = prefix + jobName + "." + fieldName;
		String value = environment.getProperty(key);
		if (StringUtils.hasText(value)) {
			return value;
		}
		return defaultValue;
	}
	
	private int getEnvironmentIntValue(String jobName, String fieldName, int defaultValue) {
		String key = prefix + jobName + "." + fieldName;
		String value = environment.getProperty(key);
		if (StringUtils.hasText(value)) {
			return Integer.valueOf(value);
		}
		return defaultValue;
	}
	
	private long getEnvironmentLongValue(String jobName, String fieldName, long defaultValue) {
		String key = prefix + jobName + "." + fieldName;
		String value = environment.getProperty(key);
		if (StringUtils.hasText(value)) {
			return Long.valueOf(value);
		}
		return defaultValue;
	}
	
	private boolean getEnvironmentBooleanValue(String jobName, String fieldName, boolean defaultValue) {
		String key = prefix + jobName + "." + fieldName;
		String value = environment.getProperty(key);
		if (StringUtils.hasText(value)) {
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}
}

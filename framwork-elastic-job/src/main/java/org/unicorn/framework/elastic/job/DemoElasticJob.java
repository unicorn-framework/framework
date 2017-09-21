package org.unicorn.framework.elastic.job;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.AbstractOneOffElasticJob;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;

@Component
public  class  DemoElasticJob extends AbstractOneOffElasticJob{
	
	
	@PostConstruct
    public void init() {
		System.out.println("init");
    } 
    @Override
	public void process(JobExecutionMultipleShardingContext context) {
    	System.out.println("elastic job");
    }
    
   
}
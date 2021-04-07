package org.unicorn.framework.elastic.dynamic.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.service.JobService;

/**
 * 动态任务添加
 *
 * <p>可以用于同一个任务，需要不同的时间来进行触发场景<p>
 *
 * @author xieibn
 */
@Api(value = "动态job模块", tags = {"动态job模块"})
@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 添加动态任务（适用于脚本逻辑已存在的情况，只是动态添加了触发的时间）
     *
     * @param job 任务信息
     * @return
     */
    @ApiOperation("动态添加job")
    @RequestMapping(value="/job",method = RequestMethod.POST)
    public ResponseDto<String> addJob(@RequestBody Job job) throws PendingException {
        try {
            jobService.addJob(job);
            return new ResponseDto<>();
        } catch (Exception e) {
            throw new PendingException(SysCode.SYS_FAIL,"添加动态任务失败");
        }

    }

    /**
     * 删除动态注册的任务（只删除注册中心中的任务信息）
     *
     * @param jobName 任务名称
     * @throws Exception
     */
    @ApiOperation("根据任务名称动态删除任务")
    @GetMapping("/job/remove/{jobName}")
    public ResponseDto<String> removeJob(@PathVariable("jobName") String jobName) {
        try {
            jobService.removeJob(jobName);
            return new ResponseDto<>();
        } catch (Exception e) {
            throw new PendingException(SysCode.SYS_FAIL,"删除任务失败");
        }

    }
}

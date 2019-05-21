package com.cn.kbyd.mybatch.task;

import com.cn.kbyd.mybatch.task.job.DataBatchJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName TimeTask
 * @Description
 * @Author yanan.wu
 * @Date 2019/5/21 9:02
 **/
@Slf4j
@Component
public class TimeTask {
    private final JobLauncher jobLauncher;
    private final DataBatchJob dataBatchJob;

    @Autowired
    public TimeTask(JobLauncher jobLauncher, DataBatchJob dataBatchJob) {
        this.jobLauncher = jobLauncher;
        this.dataBatchJob = dataBatchJob;
    }

    // 定时任务，每十秒执行一次
    @Scheduled(cron = "0/10 * * * * ?")
    public void runBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        log.info("定时任务执行了...");
        // 在运行一个job的时候需要添加至少一个参数，这个参数最后会被写到batch_job_execution_params表中，
        // 不添加这个参数的话，job不会运行，并且这个参数在表中中不能重复，若设置的参数已存在表中，则会抛出异常，
        // 所以这里才使用时间戳作为参数
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        // 获取job并运行
        Job job = dataBatchJob.dataHandleJob();
        JobExecution execution = jobLauncher.run(job, jobParameters);
        log.info("定时任务结束. Exit Status : {}", execution.getStatus());
    }
}

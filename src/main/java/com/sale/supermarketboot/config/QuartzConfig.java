package com.sale.supermarketboot.config;

import com.sale.supermarketboot.job.TestTask1;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:45
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail taskQuartz1() {
        return JobBuilder.newJob(TestTask1.class).withIdentity("testTask1").storeDurably().build();
    }
    @Bean
    public Trigger testQuartzTrigger1(){
        //cron方式，每隔5秒执行一次
        return TriggerBuilder.newTrigger().forJob(taskQuartz1())
                .withIdentity("testTask1")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();

    }

//    @Bean
//    public JobDetail testQuartz2() {
//        return JobBuilder.newJob(TestTask2.class).withIdentity("testTask2").storeDurably().build();
//    }
//
//    @Bean
//    public Trigger testQuartzTrigger2() {
//        //cron方式，每隔5秒执行一次
//        return TriggerBuilder.newTrigger().forJob(testQuartz2())
//                .withIdentity("testTask2")
//                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
//                .build();
//    }


}

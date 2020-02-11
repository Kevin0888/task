package com.sale.supermarketboot.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:43
 */
public class TestTask1 extends QuartzJobBean{
@Autowired
private  CommodityJob commodityJob;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println(" start true code------");
        try {
            commodityJob.commodityHandle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" end true code------");
    }
}

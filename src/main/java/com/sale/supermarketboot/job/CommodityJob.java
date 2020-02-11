package com.sale.supermarketboot.job;

import com.alibaba.fastjson.JSON;
import com.sale.supermarketboot.enums.TaskStatus;
import com.sale.supermarketboot.enums.TaskType;
import com.sale.supermarketboot.pojo.Commodity;
import com.sale.supermarketboot.pojo.Task;
import com.sale.supermarketboot.service.SuperMarketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
//@EnableScheduling
public class CommodityJob {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    @Autowired
    SuperMarketService superMarketService;

    /**
     * 定时任务
     *
     * @throws Exception·
     */
//    @Scheduled(fixedRate = 10000) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行
    public void commodityHandle() throws Exception {
        //查询当前时间前3分钟的，状态为非成功的，类型为预审的任务列表
        long beforeDate = System.currentTimeMillis() - 3 * 60 * 1000;
        List<Task> list = superMarketService.selectTask(TaskStatus.TASK_SUCCESS.getCode(), new Timestamp(beforeDate), TaskType.TASK_COMMODITY.getCode());
        if (!CollectionUtils.isEmpty(list)) {
            //把查到的任务状态都改为执行中，防止因为延时定时器再次查到而重复执行
            updateStatus(list);
            for (Task task : list) {
                executeTask(task);
            }
        }
        logger.info("-----------------没有要执行的任务执行---------------------");
        System.out.println("当前没有要执行的任务执行-----------------------------------------");
    }

    /**
     * 更新任务状态，转为map
     *
     * @param list
     */
    public void updateStatus(List<Task> list) {
        for (Task task : list) {
            task.setStatus(TaskStatus.TASK_DOING.getCode());
            superMarketService.updateTask(task);
        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("status", TaskStatus.TASK_DOING.getCode());
//        final List<Long> ids = list.stream().map(item -> item.getId()).collect(Collectors.toList());
//        map.put("idList", ids);
//        update(map);
    }

    /**
     * map参数的更新任务状态
     *
     * @param map
     */
    public void update(Map<String, Object> map) {
        List<Integer> list = (ArrayList) map.get("idList");
        for (int l : list) {
            Task task = new Task();
            task.setStatus(Integer.class.cast(map.get("status")));
            task.setId(l);
            superMarketService.updateTask(task);
        }
    }

    /**
     * 执行任务
     *
     * @param task
     * @throws Exception
     */
    public void executeTask(Task task) throws Exception {
        System.out.println("开始执行任务---");
        Map<String,Integer> map =JSON.parseObject(task.getMsg(),HashMap.class);
        System.out.println("准备执行方法");
        try {

            if (map!=null){
                int commodityId = map.get("commodityId");
                int count = map.get("count");
                Commodity commodity = superMarketService.getCommodity(commodityId);
                count +=commodity.getStock();
                superMarketService.updateCommodityChecked(commodityId,count);
                //更新订单为取消状态
                superMarketService.updateOrderItem(task.getOrderNumber(),commodityId,2);
                superMarketService.updateOrder(task.getOrderNumber(),0.0,0,2);
                //更新任务状态
                task.setStatus(TaskStatus.TASK_SUCCESS.getCode());
                superMarketService.updateTask(task);
                System.out.println("更新任务状态为成功");

            } else {
                // todo 根据响应码更改状态
                //更新任务状态
                task.setStatus(TaskStatus.TASK_FAILED.getCode());
                //数据库改frequency
                task.setFrequency(task.getFrequency());
                superMarketService.updateTask(task);
                System.out.println("执行完毕，更新任务状态为失败");
            }
        } catch (Exception e) {
            System.out.println("出现异常，开始更新状态为失败"+e.getMessage());
            //更新任务状态
            task.setStatus(TaskStatus.TASK_FAILED.getCode());
            task.setFrequency(task.getFrequency() + 1);
            superMarketService.updateTask(task);
            System.out.println("更新任务状态为失败");

        }

    }
}

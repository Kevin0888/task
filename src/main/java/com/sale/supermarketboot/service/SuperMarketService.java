package com.sale.supermarketboot.service;


import com.alibaba.fastjson.JSON;
import com.sale.supermarketboot.dao.*;
import com.sale.supermarketboot.enums.TaskStatus;
import com.sale.supermarketboot.enums.TaskType;
import com.sale.supermarketboot.pojo.*;
import com.sale.supermarketboot.utils.CommodityVO;
import com.sale.supermarketboot.utils.IDUtil;
import com.sale.supermarketboot.utils.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aaa
 */
@Service
public class SuperMarketService {

    @Autowired
    UserDao userDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CommodityDao commodityDao;
    @Autowired
    MemberRecordDao memberRecordDao;
    @Autowired
    TaskDao taskDao;



    /**
     * 查询管理员信息
     *
     * @param username
     * @param password
     * @return
     */
    public User getUser(String username, String password) {
        return userDao.getUser(username, password);
    }

    /**
     * 删除商品库存
     *
     * @param commodityId
     * @return
     */
    public void deleteCommodity(int commodityId) {

        commodityDao.delete(commodityId);
    }

    /**
     * 添加商品
     *
     * @param commodity
     */
    public void inputCommodity(Commodity commodity) {
        commodityDao.add(commodity);
    }

    /**
     * 查询单个商品
     *
     * @param commodityId
     * @return
     */
    public Commodity getCommodity(int commodityId) {
        return commodityDao.getCommodity(commodityId);
    }

    /**
     * 查询所有商品
     *
     * @return
     */
    public List<Commodity> getCommodities() {
        return commodityDao.getCommodities();
    }


    /**
     * 查询会员
     *
     * @param id
     * @return
     */
    public Member getMember(int id) {

        return memberDao.getMember(id);
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }

    /**
     * 添加会员
     *
     * @param member
     */
    public void addMember(Member member) {
        memberDao.add(member);
    }

    /**
     * 更新会员
     *
     * @param member
     */
    public void updateMember(Member member) {
        memberDao.update(member);
    }

    /**
     * 删除会员
     *
     * @param id
     */
    public void deleteMember(int id) {
        memberDao.delete(id);
    }

    /**
     * 添加订单
     *
     * @param shoppingNumber
     */
    public void addOrder(int shoppingNumber) {
        Order order = new Order();
        order.setOrderNumber(shoppingNumber);
        order.setCheckoutType(0);
        orderDao.add(order);
    }

    /**
     * 查询未结账的所有记录
     *
     * @param shoppingNumber
     * @return
     */
    public List<OrderItemVO> getAllUncheck(int shoppingNumber) {
        return orderItemDao.getAllUncheck(shoppingNumber);
    }


    /**
     * 查询已结账的所有记录
     *
     * @param shoppingNumber
     * @return
     */
    public List<OrderItemVO> getAllChecked(int shoppingNumber) {
        return orderItemDao.getAllChecked(shoppingNumber);
    }

    /**
     * 更新订单表
     *
     * @param orderNum
     */
    
    public void updateOrder(int orderNum, double totalPrice, int memberId,int type) {
        Order order = new Order();
        order.setOrderNumber(orderNum);
        order.setSum(totalPrice);
        order.setMemberId(memberId);
        order.setCheckoutType(type);
        orderDao.update(order);

    }

    /**
     * 查询订单详情信息
     *
     * @return
     */
    public List<OrderItem> getOrders(int orderNum) {
        return orderItemDao.get(orderNum);
    }

    /**
     * 添加订单详情
     *
     * @param orderItem
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItemDao.add(orderItem);
    }

    /**
     * 更新订单详情结账状态
     *
     * @param orderNumber
     * @param isChecked
     */
    public void updateOrderItem(int orderNumber, int commodityId, int isChecked) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderNumber(orderNumber);
        orderItem.setCommodityId(commodityId);
        orderItem.setIsChecked(isChecked);
        orderItemDao.update(orderItem);
    }

    /**
     * 更新订单详情商品数量
     *
     * @param orderNumber
     * @param count
     */
    public void updateOrderCount(int orderNumber, int commodityId, int count, double totalPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderNumber(orderNumber);
        orderItem.setCommodityId(commodityId);
        orderItem.setCount(count);
        orderItem.setTotal(totalPrice);
        orderItemDao.update(orderItem);
    }

    /**
     * 更新库存数量
     *
     * @param commodityID
     */
    public void updateCommodityChecked(int commodityID, int newStock) {
        Commodity commodity = new Commodity();
        commodity.setId(commodityID);
        commodity.setStock(newStock);
        commodityDao.update(commodity);
    }

    public void addMemberRecord(int memberID, int shopNum, double totalCost) {
        int total = new Double(totalCost).intValue();
        MemberRecord memRecord = new MemberRecord();
        memRecord.setMemberId(memberID);
        memRecord.setOrderNumber(shopNum);
        memRecord.setReceivedPoints(total);
        memRecord.setSum(totalCost);
        memberRecordDao.addRecord(memRecord);
    }

    /**
     * 现金结账流程，添加事务
     *
     * @param orderNumber
     * @param total
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int checkoutByCash(String orderNumber, String total) {
        int orderNum = Integer.parseInt(orderNumber);
        double totalCost = Double.parseDouble(total);
        //更新订单信息
        updateOrder(orderNum, totalCost, 0,1);
        //更新库存数量commdity表
        List<OrderItem> orderItemList = getOrders(orderNum);
        for (OrderItem item : orderItemList) {
            int commodityID = item.getCommodityId();
//            Commodity commodity = getCommodity(commodityID);
//            int stock = commodity.getStock();
//            int count = item.getCount();
//            int newStock = stock - count;
//            if (newStock < 0) {
//                newStock = 0;
//            }
//            updateCommodityChecked(commodityID, newStock);
            //更新订单详情状态为已结账
            int isCheck = 1;
            updateOrderItem(orderNum, commodityID, isCheck);
            //删除定时任务
            Task task = new Task();
            task.setStatus(5);
            task.setOrderNumber(orderNum);
            cancelTask(task);
        }
        return 0;
    }

    /**
     * 会员结账流程，添加事务
     *
     * @param shopNumber
     * @param total
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> checkoutByMember(String shopNumber, String total, String memberId) {
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(shopNumber.trim()) || StringUtils.isEmpty(total) || "0".equals(memberId.trim())) {
            map.put("error", 99999999);
            return map;
        }
        int shopNum = Integer.parseInt(shopNumber);
        int memberID = Integer.parseInt(memberId);
        double totalCost = Double.parseDouble(total);
        int totalMember = new Double(totalCost).intValue();
        //更新订单信息
        updateOrder(shopNum, totalCost, memberID,1);
        //更新库存数量commdity表
        List<OrderItem> orderItemList = getOrders(shopNum);
        for (OrderItem item : orderItemList) {
            int commodityID = item.getCommodityId();
//            Commodity commodity = getCommodity(commodityID);
//            int stock = commodity.getStock();
//            int count = item.getCount();
//            int newStock = stock - count;
//            if (newStock < 0) {
//                newStock = 0;
//            }
//            updateCommodityChecked(commodityID, newStock);
            //更新订单详情状态为已结账
            int isCheck = 1;
            updateOrderItem(shopNum, commodityID, isCheck);
            //删除定时任务
            Task task = new Task();
            task.setStatus(5);
            task.setOrderNumber(shopNum);
            cancelTask(task);
        }
        //添加会员消费记录
        addMemberRecord(memberID, shopNum, totalCost);
        //更新会员记录
        //查会员记录表
        Member member = getMember(memberID);
        int points = member.getPoints();
        int pointMem = points + totalMember;
        double totalMem = member.getTotal();
        double newTotal = totalMem - totalCost;
        if (newTotal < 0) {
            newTotal = 0.00;
        }
        Member m = new Member();
        m.setId(memberID);
        m.setPoints(pointMem);
        m.setTotal(newTotal);
        updateMember(m);
        map.put("points", pointMem);
        map.put("total", newTotal);
        map.put("error", 00000000);
        return map;

    }

    /**
     * 添加商品流程，添加事务
     *
     * @param commodityID
     * @param count
     * @param shoppingNumStr
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addCommodity(String commodityID, String count, String shoppingNumStr) {
        int commodityCount = Integer.parseInt(count);
        int commodityId = Integer.parseInt(commodityID);

        //根据Id查商品详情
        Commodity commodity = getCommodity(Integer.parseInt(commodityID));
        if (commodity == null || commodity.getStock() < 0) {
            return 0;
        }
        int newStock = 0;
        //库存不足，购买数量=库存
        if (commodity.getStock() < commodityCount) {
            commodityCount = commodity.getStock();
        } else {
            //新库存=当前库存减去购买数量
            newStock = commodity.getStock() - commodityCount;
        }
        //根据流水号判断是否添加订单
        int shoppingNumber = Integer.parseInt(shoppingNumStr);
        if (shoppingNumber == 0) {
            //初次购买，添加订单记录
            shoppingNumber = IDUtil.getId();
            addOrder(shoppingNumber);
        }
        //查询是否存在相同的订单商品
        OrderItem sameOrder = getSameOrder(commodityId, shoppingNumber);
        //没有相同订单，添加记录
        if (sameOrder == null) {
            //转换实体CommodityOV，
            CommodityVO commodityVO = new CommodityVO();
            commodityVO.setPrice(commodity.getPrice());
            commodityVO.setCount(commodityCount);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNumber(shoppingNumber);
            orderItem.setCommodityId(commodityId);
            orderItem.setCommodityName(commodity.getName());
            orderItem.setPrice(commodity.getPrice());
            orderItem.setCount(commodityCount);
            orderItem.setTotal(commodityVO.getTotalPrice());
            orderItem.setIsChecked(0);
            //添加订单详情
            addOrderItem(orderItem);
            //更新库存
            updateCommodityChecked(commodityId, newStock);
            //添加定时任务
            saveTask(shoppingNumber,commodityId,commodityCount);
            return shoppingNumber;
        }
        //存在相同订单，更新记录
        commodityCount += sameOrder.getCount();
        //如果库存小于购买数量，购买数量=库存
        if (commodity.getStock() < commodityCount) {
            commodityCount = commodity.getStock();
        }
        //转换实体CommodityOV
        double totalPrice = commodityCount * commodity.getPrice();
        //更新订单详情
        updateOrderCount(shoppingNumber, commodityId, commodityCount, totalPrice);
        //更新库存
        updateCommodityChecked(commodityId, newStock);
        //添加定时任务
        saveTask(shoppingNumber,commodityId,commodityCount);
        return shoppingNumber;
    }

    /**
     * 查询相同订单的商品
     *
     * @param commodityID
     * @param shoppingNumber
     * @return
     */
    public OrderItem getSameOrder(int commodityID, int shoppingNumber) {
        return orderItemDao.getSameOrder(shoppingNumber, commodityID);
    }

    /**
     * 查询task定时任务
     * @return
     */
    public List<Task> selectTask(Integer status, Timestamp timestamp, String dataType){
        return taskDao.selectTask(status,timestamp,dataType);
    }

    /**
     * 更新定时任务
     * @param param
     */
    public void updateTask(Task param){
        taskDao.updateTask(param);
    }

    /**
     * 取消定时任务
     * @param param
     */
    public void cancelTask(Task param){
        taskDao.cancelTask(param);
    }
    /**
     * 创建定时任务
     * @param orderNum
     * @param commodityId
     * @param count
     */
    public void  saveTask(int orderNum,int commodityId,int count){

        Task task = new Task();
        Map<String,Integer> map = new HashMap();
        map.put("commodityId", commodityId);
        map.put("count", count);
        task.setMsg(JSON.toJSONString(map));
        task.setOrderNumber(orderNum);
        task.setCreateTime(new Timestamp(System.currentTimeMillis()));
        task.setFrequency(0);
        task.setDataType(TaskType.TASK_COMMODITY.getCode());
        task.setRemark("order");
        task.setStatus(TaskStatus.TASK_UNDO.getCode());
        taskDao.saveTask(task);
    }

}



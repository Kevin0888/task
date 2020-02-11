package com.sale.supermarketboot.dao;


import com.sale.supermarketboot.pojo.OrderItem;
import com.sale.supermarketboot.utils.OrderItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderItemDao {
    void delete();
    void update(OrderItem param);
    List<OrderItem> get(@Param("orderNum") int orderNum);
    void add(OrderItem orderItem);

    List<OrderItemVO> getAllUncheck(@Param("shopNum") int shopNum);
    List<OrderItemVO> getAllChecked(@Param("shopNum") int shopNum);

    OrderItem getSameOrder(@Param("shopNum") int shopNum,@Param("commodityId") int commodityId);
}

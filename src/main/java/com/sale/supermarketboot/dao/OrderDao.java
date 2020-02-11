package com.sale.supermarketboot.dao;


import com.sale.supermarketboot.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {
    void delete();
    void update(Order order);
    List<Order> get();
    void add(Order order);
}

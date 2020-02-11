package com.sale.supermarketboot.dao;

import com.sale.supermarketboot.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    void delete();
    void update(User param);
    User getUser(@Param("username") String username, @Param("password") String password);
    void add();
}
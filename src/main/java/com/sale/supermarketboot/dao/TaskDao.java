package com.sale.supermarketboot.dao;


import com.sale.supermarketboot.pojo.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskDao {

    void saveTask(Task param);
    void updateTask(Task param);
    List<Task> selectTask(@Param("status") Integer status, @Param("timestamp") Timestamp timestamp, @Param("dataType") String dataType);
    void deleteTask();

    void cancelTask(Task param);
}

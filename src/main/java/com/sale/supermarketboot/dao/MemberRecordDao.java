package com.sale.supermarketboot.dao;

import com.sale.supermarketboot.pojo.MemberRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemberRecordDao {
    void deleteRecord();
    void updateRecord(MemberRecord param);
    List<MemberRecord> getRecord();
    void addRecord(MemberRecord param);
}

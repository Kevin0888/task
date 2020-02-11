package com.sale.supermarketboot.dao;


import com.sale.supermarketboot.pojo.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDao {
    void delete(@Param("id") int id);
    void update(Member param);
    Member getMember(@Param("id") int id);
    void add(Member member);
    List<Member> getAllMembers();
}

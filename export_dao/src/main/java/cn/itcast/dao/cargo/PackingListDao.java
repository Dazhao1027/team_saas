package cn.itcast.dao.cargo;


import cn.itcast.domain.cargo.PackingList;
import cn.itcast.domain.cargo.PackingListExample;

import java.util.List;

public interface PackingListDao {
    int deleteByPrimaryKey(String packingListId);

    int insert(PackingList record);

    int insertSelective(PackingList record);

    List<PackingList> selectByExample(PackingListExample example);

    PackingList selectByPrimaryKey(String packingListId);

    int updateByPrimaryKeySelective(PackingList record);

    int updateByPrimaryKey(PackingList record);
}
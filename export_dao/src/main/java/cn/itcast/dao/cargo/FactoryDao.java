package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;

import java.util.List;

public interface FactoryDao {
    int deleteByPrimaryKey(String id);

    int insert(Factory record);

    int insertSelective(Factory record);

    List<Factory> selectByExample(FactoryExample example);

    Factory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Factory record);

    int updateByPrimaryKey(Factory record);
}
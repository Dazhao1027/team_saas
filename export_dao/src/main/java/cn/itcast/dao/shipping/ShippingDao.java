package cn.itcast.dao.shipping;

import cn.itcast.domain.shipping.Shipping;
import cn.itcast.domain.shipping.ShippingExample;
import cn.itcast.domain.vo.ShippingVo;

import java.util.List;

public interface ShippingDao {
    //根据ID删除
    int deleteByPrimaryKey(String shippingOrderId);
    //根据ID添加
    int insert(Shipping record);
    //条件添加
    int insertSelective(Shipping record);
    //条件查询
    List<Shipping> selectByExample(ShippingExample example);
    //根据ID查询
    Shipping selectByPrimaryKey(String shippingOrderId);
    //修改
    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    //细粒度权限控制，查询某个部门及其下属部门创建的购销合同
    List<Shipping> findByDeptId(String deptId);

    //根据委托表ID查询,然后导出
    ShippingVo exportShippingVo(String shippingOrderId);
}
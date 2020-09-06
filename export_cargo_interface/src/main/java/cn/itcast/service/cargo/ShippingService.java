package cn.itcast.service.cargo;


import cn.itcast.domain.shipping.Shipping;
import cn.itcast.domain.shipping.ShippingExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ShippingService {
    //委托接口
    //查询委托单页面
    PageInfo<Shipping> findByPage(ShippingExample shippingExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Shipping> findAll(ShippingExample shippingExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Shipping findById(String id);

    /**
     * 新增
     */
    void save(Shipping shipping,String id);

    /**
     * 修改
     */
    void update(Shipping shipping);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 细粒度权限控制，查询某个部门及其下属部门创建的购销合同
     * @param deptId  部门id，查询条件,根据basecontroller的方法查找
     * @param pageNum  当前页
     * @param pageSize 页大小
     * @return
     */
    PageInfo<Shipping> findByDeptId(String deptId, Integer pageNum, Integer pageSize);
}

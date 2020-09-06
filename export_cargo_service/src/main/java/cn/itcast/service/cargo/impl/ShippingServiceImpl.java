package cn.itcast.service.cargo.impl;


import cn.itcast.dao.cargo.PackingListDao;
import cn.itcast.dao.shipping.ShippingDao;

import cn.itcast.domain.cargo.PackingList;
import cn.itcast.domain.shipping.Shipping;
import cn.itcast.domain.shipping.ShippingExample;
import cn.itcast.service.cargo.ShippingService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
@Service(timeout = 1000000)
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private PackingListDao packingListDao;
    //查找页面
    @Override
    public PageInfo<Shipping> findByPage(ShippingExample shippingExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> list = shippingDao.selectByExample(shippingExample);
        PageInfo<Shipping> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询所有
     *
     * @param shippingExample
     */
    @Override
    public List<Shipping> findAll(ShippingExample shippingExample) {
        List<Shipping> list = shippingDao.selectByExample(shippingExample);
        return list;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Shipping findById(String id) {
        Shipping shipping = shippingDao.selectByPrimaryKey(id);
        return shipping;
    }

    /**
     * 新增
     *
     * @param shipping
     */
    @Override
    public void save(Shipping shipping,String id) {
        //根据装箱单ID设置主键ID
        shipping.setShippingOrderId(id);
        //添加建表日期
        shipping.setCreateTime(new Date());
        //将状态设为草稿
        shipping.setState(0);
        //装箱单生成委托单后，要修改装箱单状态为1，表示已生成委托。不能再重复生成委托单。
        PackingList packingList = packingListDao.selectByPrimaryKey(id);
        packingList.setState(1L);
        packingListDao.updateByPrimaryKeySelective(packingList);
        shippingDao.insert(shipping);
    }

    /**
     * 修改
     *
     * @param shipping
     */
    @Override
    public void update(Shipping shipping) {
        shippingDao.updateByPrimaryKey(shipping);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        shippingDao.deleteByPrimaryKey(id);
    }

    /**
     * 细粒度权限控制，查询某个部门及其下属部门创建的购销合同
     *
     * @param deptId   部门id，查询条件,根据basecontroller的方法查找
     * @param pageNum  当前页
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo<Shipping> findByDeptId(String deptId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> list = shippingDao.findByDeptId(deptId);
        PageInfo<Shipping> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}

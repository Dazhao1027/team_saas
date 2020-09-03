package cn.itcast.dao.factory;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class FactoryDaoTest {
    @Autowired
    private FactoryDao factoryDao;

    // 主键查询
    @Test
    public void selectByPrimaryKey() {
        Factory factory = factoryDao.selectByPrimaryKey("1");
        System.out.println(factory);
    }

    /**
     * 全字段更新
     * 日志：Column 'create_time' cannot be null 字段非空
     * update co_factory set ctype = ?, full_name = ?, factory_name = ?, contacts = ?,
     * phone = ?, mobile = ?, fax = ?, address = ?, inspector = ?, remark = ?,
     * order_no = ?, state = ?, create_by = ?, create_dept = ?, create_time = ?,
     * update_by = ?, update_time = ? where id = ?
     */
    @Test
    public void update() {
        Factory factory = new Factory();
        factory.setId("1d3125df-b186-4a2d-8048-df817d30e52b");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factory.setCtype("测试..");
        factory.setFullName("草原工厂");
        factoryDao.updateByPrimaryKey(factory);
    }

    /**
     * 有选择的更新：对象属性有值，才生成更新的字段。
     * update co_factory SET create_time = ?, update_time = ? where id = ?
     */
    @Test
    public void updateSelection() {
        Factory factory = new Factory();
        factory.setId("1d3125df-b186-4a2d-8048-df817d30e52b");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    // 条件查询：factory_name=升华
    @Test
    public void findByExample() {
        // 1. 构造条件对象
        FactoryExample example = new FactoryExample();
        // 1.1 获取封装条件的Criteria条件对象
        FactoryExample.Criteria criteria = example.createCriteria();
        // 1.2 添加条件
        //criteria.andFactoryNameLike("升_"); 下划线匹配其后的任意一个字符
        criteria.andFactoryNameLike("升%");

        // 2. 条件查询
        List<Factory> list = factoryDao.selectByExample(example);
        // 3. 测试
        System.out.println(list);
    }
}

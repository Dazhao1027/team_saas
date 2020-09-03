package cn.itcast.dao.system;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.system.Dept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class DeptDaoTest {

    @Autowired
    private DeptDao deptDao;

    @Test
    public void test() {
        String companyId = "1";
        List<Dept> list = deptDao.findAll(companyId);
        System.out.println(list);
    }

    @Test
    public void test2() {
        System.out.println(deptDao.findById("100101"));
    }
}

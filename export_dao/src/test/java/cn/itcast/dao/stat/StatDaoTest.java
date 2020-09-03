package cn.itcast.dao.stat;

import cn.itcast.dao.company.CompanyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class StatDaoTest {

    @Autowired
    private StatDao statDao;

    @Test
    public void test() {
        // [{name=众鑫, value=489.00}, {name=会龙, value=26201.40}]
        System.out.println(statDao.factorySale());
    }
}

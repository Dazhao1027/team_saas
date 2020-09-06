package cn.itcast.dao.shipping;

import cn.itcast.domain.shipping.Shipping;
import cn.itcast.domain.vo.ShippingVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class shippingdaoTest {
    @Autowired
    private ShippingDao shippingDao;
    @Test
    public void shippingdao(){
        ShippingVo vo = shippingDao.exportShippingVo("8a7e842b57bc33760157bc340dad0000");
        System.out.println(vo);
    }
}

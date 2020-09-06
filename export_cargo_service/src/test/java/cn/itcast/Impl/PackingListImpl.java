package cn.itcast.Impl;


import cn.itcast.domain.cargo.PackingList;
import cn.itcast.service.cargo.PackingListService;
import cn.itcast.service.cargo.impl.PackingListServiceImpl;
import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class PackingListImpl {
    @Reference
    private PackingListService packingListService;
    @Test
    public void packing(){
        PackingList pack = packingListService.findById("1");
        System.out.println(pack);
    }

}

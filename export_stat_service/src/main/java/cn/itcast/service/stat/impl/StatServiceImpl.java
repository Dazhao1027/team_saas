package cn.itcast.service.stat.impl;

import cn.itcast.dao.stat.StatDao;
import cn.itcast.service.stat.StatService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Override
    public List<Map<String, Object>> factorySale() {
        return statDao.factorySale();
    }

    @Override
    public List<Map<String, Object>> productSale() {
        return statDao.productSale();
    }

    @Override
    public List<Map<String, Object>> online() {
        return statDao.online();
    }
}

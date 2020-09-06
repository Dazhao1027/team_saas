package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.PackingListDao;

import cn.itcast.domain.cargo.PackingList;
import cn.itcast.domain.cargo.PackingListExample;

import cn.itcast.service.cargo.PackingListService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service(timeout = 1000000)
public class PackingListServiceImpl implements PackingListService {
    @Autowired
    private PackingListDao packingListDao;
    @Override
    public int delete(String packingListId) {

        return 0;
    }

    @Override
    public int save(PackingList record) {
        return 0;
    }

    @Override
    public List<PackingList> findAll(PackingListExample example) {
        return null;
    }



    @Override
    public PackingList findById(String packingListId) {
        //根据ID查询
        PackingList packingList = packingListDao.selectByPrimaryKey(packingListId);
        return packingList;
    }

    @Override
    public int update(PackingList record) {
        return 0;
    }
}

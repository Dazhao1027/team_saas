package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ExportProductDao;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.service.cargo.ExportProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// 报运单的商品
@Service
public class ExportProductServiceImpl implements ExportProductService {

    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExportProduct exportProduct) {
        exportProductDao.insertSelective(exportProduct);
    }

    @Override
    public void update(ExportProduct exportProduct) {
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExportProduct> list = exportProductDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample example) {
        return exportProductDao.selectByExample(example);
    }
}

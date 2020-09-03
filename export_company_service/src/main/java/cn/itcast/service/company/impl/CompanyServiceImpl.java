package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

// 注入导入的包：com.alibaba.dubbo.config.annotation.Service;
@Service(timeout = 100000) // 设置超时时间，单位是毫秒,100秒
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        // 设置企业id：uuid作为主键
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {
        // 使用PageHelper提供的分页功能； 自动对其后的第一个查询自动进行分页
        // 参数1：当前页； 参数2：页大小
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao的查询方法
        List<Company> list = companyDao.findAll();
        // 封装分页结果，并返回
        PageInfo<Company> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}

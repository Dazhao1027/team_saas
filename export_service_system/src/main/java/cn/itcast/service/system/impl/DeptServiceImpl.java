package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public PageInfo<Dept> findAll(String companyId, int pageNum, int pageSize) {
        // 开启分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 查询dao
        List<Dept> list = deptDao.findAll(companyId);
        // 封装分页结果返回
        return new PageInfo<>(list);
    }

    @Override
    public Dept findById(String deptId) {
        return deptDao.findById(deptId);
    }

    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    @Override
    public void save(Dept dept) {
        // 设置主键
        dept.setId(UUID.randomUUID().toString());
        // 判断：如果没有选择上级部门，就把部门关联的parent的id设置为NULL，不能为""(外键)
        //if (dept.getParent() != null && "".equals(dept.getParent().getId())){
        //    dept.getParent().setId(null);
        //}
        deptDao.save(dept);
    }

    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    @Override
    public boolean delete(String id) {
        //-- 删除部门
        //-- 1. 先根据删除的部门id查询，是否有被引用
        Long count = deptDao.findByParent(id);

        //-- 2. 如果查询到数据，说明当前部门有子部门，不能删除。没有查询到数据才可以删除
        if (count == 0) {
            // 没有查询到子部门，可以删除
            deptDao.delete(id);
            return true;
        }
        // 删除失败
        return false;
    }
}

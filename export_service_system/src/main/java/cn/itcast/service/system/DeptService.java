package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {
    /**
     * 分页查询列表
     */
    PageInfo<Dept> findAll(String companyId,int pageNum,int pageSize);
    /**
     * 根据id查询
     */
    Dept findById(String deptId);

    /**
     * 根据企业id，查询所有部门（不分页）
     * @param companyId
     * @return
     */
    List<Dept> findAll(String companyId);

    /**
     * 添加
     * @param dept
     */
    void save(Dept dept);

    /**
     * 修改
     * @param dept
     */
    void update(Dept dept);

    /**
     * 删除
     * @param id 删除的部门的主键
     * @return 返回true表示成功；false表示失败
     */
    boolean delete(String id);
}

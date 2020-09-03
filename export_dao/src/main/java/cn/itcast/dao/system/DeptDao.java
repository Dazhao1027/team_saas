package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

public interface DeptDao {
    /**
     * 查询列表
     */
    List<Dept> findAll(String companyId);
    /**
     * 根据id查询
     */
    Dept findById(String deptId);

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
     * 先根据删除的部门id查询，是否有被引用
     */
    Long findByParent(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}

package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;

import java.util.List;

public interface CompanyDao {
    /**
     * 查询列表
     */
    List<Company> findAll();

    /**
     * 添加
     */
    void save(Company company);

    /**
     * 修改
     * @param company
     */
    void update(Company company);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}

package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {
    /**
     * 分页查询列表
     */
    PageInfo<Module> findByPage(int pageNum, int pageSize);
    /**
     * 根据id查询
     */
    Module findById(String moduleId);

    /**
     * 查询所有
     */
    List<Module> findAll();

    /**
     * 添加
     * @param module
     */
    void save(Module module);

    /**
     * 修改
     * @param module
     */
    void update(Module module);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 查询角色已经拥有的权限
     * @param roleId
     * @return
     */
    List<Module> findModuleByRoleId(String roleId);

    /**
     * 查询用户的权限
     * @param userId
     * @return
     */
    List<Module> findModuleByUserId(String userId);
}

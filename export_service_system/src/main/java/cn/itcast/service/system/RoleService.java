package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    /**
     * 分页查询列表
     */
    PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize);
    /**
     * 根据id查询
     */
    Role findById(String roleId);

    /**
     * 根据企业id，查询所有
     * @param companyId
     * @return
     */
    List<Role> findAll(String companyId);

    /**
     * 添加
     * @param role
     */
    void save(Role role);

    /**
     * 修改
     * @param role
     */
    void update(Role role);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 角色分配权限
     * @param roleId
     * @param moduleIds
     */
    void updateRoleModule(String roleId, String moduleIds);

    /**
     * 查询用户已经拥有的角色
     * @param userId 根据用户id查询角色
     * @return
     */
    List<Role> findUserRoleByUserId(String userId);
}

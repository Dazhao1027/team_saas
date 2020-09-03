package cn.itcast.service.system;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    /**
     * 分页查询列表
     */
    PageInfo<User> findByPage(String companyId, int pageNum, int pageSize);
    /**
     * 根据id查询
     */
    User findById(String userId);

    /**
     * 根据企业id，查询所有
     * @param companyId
     * @return
     */
    List<User> findAll(String companyId);

    /**
     * 添加
     * @param user
     */
    void save(User user);

    /**
     * 修改
     * @param user
     */
    void update(User user);

    /**
     * 删除
     */
    boolean delete(String id);

    /**
     * 用户分配角色
     * @param userId 用户id
     * @param roleIds 用户选中的角色id数组
     */
    void changeRole(String userId, String[] roleIds);

    /**
     * 根据email查询
     * @param email
     * @return
     */
    User findByEmail(String email);
}

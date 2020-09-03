package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

	//根据id删除
    void delete(String id);

	//添加
    void save(Role role);

	//更新
    void update(Role role);

    //解除角色权限的关系
    void deleteRoleModuleByRoleId(String roleId);

    /**
     * 角色添加权限
     * 控制器方法有多个参数：
     * 1、在接口方法形参上给参数指定别名@Param("abc")，占位符中使用别名 #{abc}
     * 2. 在占位符中使用#{arg0} 表示对应方法形参的第一个参数
     * 3. 在占位符中使用#{param1} 表示对应方法形参的第一个参数
     * 4. 通过map对象封装多个参数，占位符中对应map的key
     * 5. 通过javabean对象封装多个参数，占位符中对应bean的属性
     */
    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId);

    /**
     * 查询用户已经拥有的角色
     * @param userId 根据用户id查询角色
     * @return
     */
    List<Role> findUserRoleByUserId(String userId);
}
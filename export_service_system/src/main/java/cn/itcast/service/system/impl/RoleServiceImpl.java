package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 查询dao
        List<Role> list = roleDao.findAll(companyId);
        // 封装分页结果返回
        return new PageInfo<>(list);
    }

    @Override
    public Role findById(String roleId) {
        return roleDao.findById(roleId);
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public void save(Role role) {
        // 设置主键
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //-- 1. 解除角色权限的关系
        roleDao.deleteRoleModuleByRoleId(roleId);

        //-- 2. 角色添加权限
        if (!StringUtils.isEmpty(moduleIds)) {
            String[] array = moduleIds.split(",");
            if (array != null && array.length>0){
                for (String moduleId : array) {
                    roleDao.saveRoleModule(roleId,moduleId);
                }
            }
        }
    }

    @Override
    public List<Role> findUserRoleByUserId(String userId) {
        return roleDao.findUserRoleByUserId(userId);
    }
}















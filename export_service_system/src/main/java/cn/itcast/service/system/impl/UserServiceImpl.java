package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 查询dao
        List<User> list = userDao.findAll(companyId);
        // 封装分页结果返回
        return new PageInfo<>(list);
    }

    @Override
    public User findById(String userId) {
        return userDao.findById(userId);
    }

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public void save(User user) {
        // 设置主键
        user.setId(UUID.randomUUID().toString());
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public boolean delete(String id) {
        // 1. 先查询用户角色中间表
        // SELECT COUNT(*) FROM pe_role_user WHERE user_id='1'
        Long count = userDao.findUserRoleByUserId(id);
        // 2. 如果没有找到数据，可以删除
        // DELETE FROM pe_user WHERE user_id='1'
        if (count == 0) {
            // 说明当前删除的用户id在用户角色中间表不存在，可以删除
            userDao.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public void changeRole(String userId, String[] roleIds) {
        // 用户分配角色
        // 1. 先解除用户角色关系
        // DELETE FROM pe_role_user WHERE user_id=?
        userDao.deleteUserRoleByUserId(userId);

        // 2. 用户添加角色
        // INSERT INTO pe_role_user(user_id,role_id)VALUES(?,?)
        if (roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
















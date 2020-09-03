package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {
        // 开启分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 查询dao
        List<Module> list = moduleDao.findAll();
        // 封装分页结果返回
        return new PageInfo<>(list);
    }

    @Override
    public Module findById(String moduleId) {
        return moduleDao.findById(moduleId);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public void save(Module module) {
        // 设置主键
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    @Override
    public List<Module> findModuleByRoleId(String roleId) {
        return moduleDao.findModuleByRoleId(roleId);
    }

    @Autowired
    private UserDao userDao;
    /**
     * 用户的degree
     *  0-saas管理员
     *  1-企业管理员
     *  2-管理所有下属部门和人员
     *  3-管理本部门
     *  4-普通员工
     */
    @Override
    public List<Module> findModuleByUserId(String userId) {
        //需求:根据用户id, 查询用户模块
        User user = userDao.findById(userId);
        if (user.getDegree() == 0) {
            //1. 获取登陆用户的degree=0，saas管理员，只能查看saas模块
            //SELECT * FROM ss_module WHERE belong=0
            return moduleDao.findByBelong(0);
        }
        else if (user.getDegree() == 1) {
            //2. 登陆用户的degree=1，企业管理员，可以查看除了saas以外的权限
            //SELECT * FROM ss_module WHERE belong=1
            return moduleDao.findByBelong(1);
        }
        else {
            //3. 登陆用户的degree是其他类型，根据用户查询所拥有的权限
            return moduleDao.findModuleByUserId(userId);
        }
    }
}

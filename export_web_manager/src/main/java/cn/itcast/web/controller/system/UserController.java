package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 1. 列表
     * 注意：当前页参数默认为1，页大小默认为5
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // shiro权限校验（一）编码方式
        // 访问当前方法的用户必须具有“用户管理”的权限，否则报错。
       // SecurityUtils.getSubject().checkPermission("用户管理");

        // 企业id，从当前登陆用户获取，目前先写死
        String companyId = getLoginCompanyId();

        // 调用service查询
        PageInfo<User> pageInfo = userService.findByPage(companyId,pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "system/user/user-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        // 根据企业id，查询所有部门，作为部门的下拉列表
        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 2. 添加（2）添加
     */
    @RequestMapping("/edit")
    public String edit(User user) {
        // 从当前登陆用户获取企业id、名称(先写死)
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(user.getId())) {
            // 调用service，添加
            userService.save(user);

            // 发送消息到队列中，通过消息处理系统监听队列消息并消费：发邮件提醒
            Map<String,String> map = new HashMap<>();
            map.put("title","新员工入职提醒邮件");
            map.put("email",user.getEmail());
            map.put("content","换欢来到SAAS大家庭，我们是一个有激情的团队！");
            // 发消息
            rabbitTemplate.convertAndSend("msg.email",map);

        } else {
            // 调用service，修改
            userService.update(user);
        }
        //添加成功，重定向到列表
        return "redirect:/system/user/list";
    }

    /**
     * 3. 修改 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查询数据库
        User user = userService.findById(id);
        // 查询所有部门
        List<Dept> deptList = deptService.findAll(getLoginCompanyId());

        // 保存
        request.setAttribute("user",user);
        request.setAttribute("deptList",deptList);
        // 转发到页面，回显数据
        return "system/user/user-update";
    }

    /**
     * 4. 删除
     * result = {"message":1}  删除成功
     * result = {"message":0}  删除失败
     */
    @RequestMapping("/delete")
    @ResponseBody   // 自动把方法返回的对象转json(引入jackson)
    public Map<String,Object> delete(String id){
        // 定义返回结果
        Map<String,Object> result = new HashMap<>();

        // 调用service删除，并且进行删除校验，返回true表示删除成功
        boolean flag = userService.delete(id);
        if (flag) {
            result.put("message",1);
        } else {
            result.put("message",0);
        }
        return result;
    }

    @Autowired
    private RoleService roleService;
    /**
     * 5. 用户分配角色(1)从user-list.jsp页面点击角色，进入用户角色列表页面user-role.jsp
     */
    @RequestMapping("/roleList")
    public String roleList(String id) {
        // 5.1 根据用户id查询 【user-role.jsp 要回显用户名】
        User user = userService.findById(id);

        // 5.2 查询所有角色  【user-role.jsp 页面要显示所有角色】
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        // 5.3 查询用户已经拥有的角色 【user-role.jsp 页面要默认选中用户拥有的角色】
        List<Role> userRoleList = roleService.findUserRoleByUserId(id);
        // 用户的角色集合页面只是用来做判断默认选中，所以在这里定义一个字符串存储所有用户的角色id。页面不能遍历集合了
        String roleIds = ""; // roleIds = "1,2,3,";  --->roleIds.contains("")
        if (userRoleList != null && userRoleList.size()>0){
            for (Role userRole : userRoleList) {
                roleIds += userRole.getId() + ",";
            }
        }

        // 5.4 保存
        request.setAttribute("user",user);
        request.setAttribute("roleList",roleList);
        request.setAttribute("roleIds",roleIds);
        return "system/user/user-role";
    }

    /**
     * 6. 用户分配角色(2)保存
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userId,String[] roleIds) {
        userService.changeRole(userId,roleIds);
        //添加成功，重定向到列表
        return "redirect:/system/user/list";
    }

}

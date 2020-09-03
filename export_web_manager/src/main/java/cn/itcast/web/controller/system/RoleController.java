package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController{

    @Autowired
    private RoleService roleService;

    /**
     * 1. 列表
     * 注意：当前页参数默认为1，页大小默认为5
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 企业id，从当前登陆用户获取，目前先写死
        String companyId = getLoginCompanyId();

        // 调用service查询
        PageInfo<Role> pageInfo = roleService.findByPage(companyId,pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "system/role/role-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "system/role/role-add";
    }

    /**
     * 2. 添加（2）添加
     */
    @RequestMapping("/edit")
    public String edit(Role role) {
        // 从当前登陆用户获取企业id、名称(先写死)
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(role.getId())) {
            // 调用service，添加
            roleService.save(role);
        } else {
            // 调用service，修改
            roleService.update(role);
        }
        //添加成功，重定向到列表
        return "redirect:/system/role/list";
    }

    /**
     * 3. 修改 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "system/role/role-update";
    }

    /**
     * 4. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list";
    }

    /**
     * 5. 角色分配权限
     * 功能入口：角色列表点击权限
     * 请求地址：http://localhost:8080/system/role/roleModule.do?roleId=4
     * 请求参数：roleId 角色id
     * 响应地址：role-module.jsp   (目前只回显角色名称，后续再异步请求初始化ztree)
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleId){
        Role role = roleService.findById(roleId);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    @Autowired
    private ModuleService moduleService;
    /**
     * 6. 角色分配权限 - 页面异步请求返回ztree需要的json数据
     * 功能入口：role-module.jsp 发送异步请求
     * 请求地址：http://localhost:8080/system/role/getZtreeNode
     * 请求参数：roleId 角色id
     * 响应数据：[{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true},{}..]
     */
    @RequestMapping("/getZtreeNode")
    @ResponseBody   // 方法返回的对象转json： [{},{}]
    public List<Map<String,Object>> getZtreeNode(String roleId){
        //1. 返回结果
        List<Map<String,Object>> result = new ArrayList<>();

        //2. 查询所有的权限
        List<Module> list = moduleService.findAll();

        //3. 查询角色已经拥有的权限.为什么？ 页面默认选中
        List<Module> roleModuleList = moduleService.findModuleByRoleId(roleId);

        //4. 遍历权限，封装返回结果
        for (Module module : list) {
            // 创建map，封装权限信息
            Map<String,Object> map = new HashMap<>();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            map.put("open",true);
            // 判断
//            if (roleModuleList != null && roleModuleList.size()>0){
//                for (Module temp : roleModuleList){
//                    if (temp.getId().equals(module.getId())){
//                        map.put("checked",true);
//                    }
//                }
//            }
            // 或者
            if (roleModuleList.contains(module)){ // 注意Module对象要重写equals方法
                map.put("checked",true);
            }
            // map添加到集合
            result.add(map);
        }
        return result;
    }


    /**
     * 7. 角色分配权限 最后的实现，角色添加权限存储到数据库中
     * 功能入口：role-module.jsp 点击保存
     * 请求地址：http://localhost:8080/system/role/updateRoleModule
     * 请求参数：
     *      roleId 角色id
     *      mouduleIds 权限id，多个用逗号隔开
     * 响应地址：
     *      重定向到角色列表
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds){
        roleService.updateRoleModule(roleId,moduleIds);
        return "redirect:/system/role/list";
    }
}















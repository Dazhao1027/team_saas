package cn.itcast.web.controller.system;

import cn.itcast.domain.company.Company;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.company.CompanyService;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController{

    @Autowired
    private DeptService deptService;

    /**
     * 1. 部门列表
     * 注意：当前页参数默认为1，页大小默认为5
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 企业id，从当前登陆用户获取，目前先写死
        String companyId = getLoginCompanyId();

        // 调用service查询
        PageInfo<Dept> pageInfo = deptService.findAll(companyId,pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "system/dept/dept-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        // 根据企业id，查询所有部门（不分页），作为页面上级部门的下拉列表
        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);
        return "system/dept/dept-add";
    }

    /**
     * 2. 添加（2）添加
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {
        // 从当前登陆用户获取企业id、名称(先写死)
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(dept.getId())) {
            // 调用service，添加
            deptService.save(dept);
        } else {
            // 调用service，修改
            deptService.update(dept);
        }
        //添加成功，重定向到列表
        return "redirect:/system/dept/list";
    }

    /**
     * 3. 修改 进入修改页面
     * A. 根据id查询
     * B. 查询全部部门，作为上级部门下拉列表显示
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查询数据库
        Dept dept = deptService.findById(id);
        // 查询所有部门
        List<Dept> deptList = deptService.findAll(getLoginCompanyId());

        // 保存
        request.setAttribute("dept",dept);
        request.setAttribute("deptList",deptList);
        // 转发到页面，回显数据
        return "system/dept/dept-update";
    }

    /**
     * 4. 删除
     */
    @RequestMapping("/delete")
    @ResponseBody   // 返回json字符串
    public String delete(String id){
        // 定义方法返回的结果
        String result = "ok";

        // 调用service删除，获取返回结果：是否删除成功
        boolean flag = deptService.delete(id);
        if (!flag){
            result = "error";  // 删除失败
        }

        return result;
    }

}

package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController{

    @Autowired
    private ModuleService moduleService;

    /**
     * 1. 列表
     * 注意：当前页参数默认为1，页大小默认为5
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 调用service查询
        PageInfo<Module> pageInfo = moduleService.findByPage(pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "system/module/module-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        // 查询所有菜单，作为上级菜单的下拉列表回显
        List<Module> menus = moduleService.findAll();
        request.setAttribute("menus",menus);
        return "system/module/module-add";
    }

    /**
     * 2. 添加（2）添加
     */
    @RequestMapping("/edit")
    public String edit(Module module) {
        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(module.getId())) {
            // 调用service，添加
            moduleService.save(module);
        } else {
            // 调用service，修改
            moduleService.update(module);
        }
        //添加成功，重定向到列表
        return "redirect:/system/module/list";
    }

    /**
     * 3. 修改 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查询
        Module module = moduleService.findById(id);
        request.setAttribute("module",module);

        // 查询所有菜单，作为上级菜单的下拉列表回显
        List<Module> menus = moduleService.findAll();
        request.setAttribute("menus",menus);
        return "system/module/module-update";
    }

    /**
     * 4. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        moduleService.delete(id);
        return "redirect:/system/module/list";
    }

}

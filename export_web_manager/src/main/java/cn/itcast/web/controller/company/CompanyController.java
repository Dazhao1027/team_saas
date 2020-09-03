package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    // 通过dubbo提供的依赖注入的注解，注入服务接口的代理对象
    @Reference
    private CompanyService companyService;

    /**
     * 1. 列表
     * 请求路径：http://localhost:8080/company/list
     * 响应路径：/WEB-INF/pages/company/company-list.jsp
     * 视图解析器：
     *      前缀：/WEB-INF/pages/
     *      后缀：.jsp
     */
    @RequestMapping("/list")
    public String list(Model model) {
        List<Company> list = companyService.findAll();
        model.addAttribute("list", list);
        return "company/company-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model) {
        return "company/company-add";
    }

    /**
     * 2. 添加（2）添加
     * 添加和修改：
     * 1. 相同点： 接收参数、跳转都是一样
     * 2. 不同点： 调用service方法不一样、主键是否为空（添加，主键为空；修改主键不为空）
     */
    @RequestMapping("/edit")
    public String edit(Company company) {
        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(company.getId())) {
            // 调用service，添加
            companyService.save(company);
        } else {
            // 调用service，修改
            companyService.update(company);
        }
        //添加成功，重定向到列表
        return "redirect:/company/list";
    }

    /**
     * 3. 修改 - 进入修改页面
     * 功能入口：
     *          在company-list.jsp企业列表页面，点击·编辑·按钮
     * 请求地址：
     *          http://localhost:8080/company/toUpdate
     * 请求参数：
     *          id  要修改的企业主键id
     * 响应地址：
     *          /WEB-INF/pages/company/company-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,Model model){
        // 根据id查询数据库
        Company company = companyService.findById(id);
        // 保存
        model.addAttribute("company",company);
        // 转发到页面，回显数据
        return "company/company-update";
    }

    /**
     * 4. 删除
     * 功能入口：企业列表点击删除
     * 请求地址：http://localhost:8080/company/delete
     * 请求参数：id 删除的企业id
     * 响应地址：重定向到列表
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        companyService.delete(id);
        //添加成功，重定向到列表
        return "redirect:/company/list";
    }

}

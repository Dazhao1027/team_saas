package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.util.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 附件模块
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    // 注入dubbo接口代理对象 (要提供service接口实现类，dubbo服务类)
    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    /**
     * 1. 附件列表
     * 请求地址：
     *   http://localhost:8080/cargo/extCproduct/list.do
     * 请求参数：
     *   contractId=1f&contractProductId=1  列表显示没有用到，主要是为了下一步的保存
     */
    @RequestMapping("/list")
    public String list(String contractId,String contractProductId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        //1. 查询附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //2. 根据购销合同id，查询货物
        ExtCproductExample extCproductExample = new ExtCproductExample();
        // 查询条件：contract_product_id 货物id
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> pageInfo =
                extCproductService.findByPage(extCproductExample, pageNum, pageSize);

        //3. 保存数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "cargo/extc/extc-list";
    }

    /**
     * 2. 附件添加、修改
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct) {
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(extCproduct.getId())) {
            // 调用service，添加
            extCproductService.save(extCproduct);
        } else {
            // 调用service，修改
            extCproductService.update(extCproduct);
        }
        //添加成功，重定向到列表 [注意：参数的传递，列表方法需要什么参数就要传递对应的参数]
        return "redirect:/cargo/extCproduct/list?contractProductId="
                +extCproduct.getContractProductId()+"&contractId="+extCproduct.getContractId();
    }

    /**
     * 3. 进入修改页面
     * http://localhost:8080/cargo/extCproduct/toUpdate.do?id=1&contractId=2&contractProductId=f
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId) {
        // 根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);

        //查询附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        // 保存 - 附件对象
        request.setAttribute("extCproduct",extCproduct);
        // 保存 - 厂家列表
        request.setAttribute("factoryList",factoryList);
        // 保存 - 购销合同、货物id (页面隐藏域取值是：${contractId}  )
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "cargo/extc/extc-update";
    }

    /**
     * 4. 删除附件
     * http://localhost:8080/cargo/extCproduct/delete.do?id=1&contractId=1&contractProductId=1
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId) {
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list?contractProductId="
                +contractProductId+"&contractId="+contractId;
    }
}
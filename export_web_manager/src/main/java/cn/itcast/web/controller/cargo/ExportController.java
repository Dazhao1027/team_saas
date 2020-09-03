package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.domain.system.User;
import cn.itcast.domain.vo.ExportProductVo;
import cn.itcast.domain.vo.ExportResult;
import cn.itcast.domain.vo.ExportVo;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Produces;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController{

    // 注入dubbo接口代理对象
    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 1. 合同管理，只显示已提交的购销合同。state=1
     * http://localhost:8080/cargo/export/contractList.do
     */
    @RequestMapping("/contractList")
    public String contractList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 构造查询条件
        ContractExample example = new ContractExample();
        // 排序：create_time 创建时间降序排序
        example.setOrderByClause("create_time desc");
        ContractExample.Criteria criteria = example.createCriteria();
        // 查询条件：company_id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        // 查询条件：state
        criteria.andStateEqualTo(1);
        PageInfo<Contract> pageInfo = contractService.findByPage(example,pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "cargo/export/export-contractList";
    }


    /**
     * 报运单列表
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 构造查询条件
        ExportExample example = new ExportExample();
        // 排序：create_time 创建时间降序排序
        example.setOrderByClause("create_time desc");
        ExportExample.Criteria criteria = example.createCriteria();
        // 查询条件：company_id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        PageInfo<Export> pageInfo = exportService.findByPage(example,pageNum,pageSize);

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "cargo/export/export-list";
    }

    /**
     * 合同管理，点击报运，进入报运单的添加页面
     * @param id 购销合同id，多个id自动以逗号隔开
     * @return
     */
    @RequestMapping("/toExport")
    public String toExport(String id){
        request.setAttribute("id",id);
        // 进入报运单的添加页面
        return "cargo/export/export-toExport";
    }

    /**
     * 添加或修改报运单
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(export.getId())) {
            export.setCreateTime(new Date());
            exportService.save(export);
        } else {
            exportService.update(export);
        }
        return "redirect:/cargo/export/list";
    }

    /**
     * 进入修改页面
     * http://localhost:8080/cargo/export/toUpdate.do?id=0
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 1. 根据报运单id查询
        Export export = exportService.findById(id);

        // 2. 查询报运单下的商品, 查询条件：报运单id
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(example);

        // 3. 保存
        request.setAttribute("export",export);
        request.setAttribute("eps",exportProductList);

        return "cargo/export/export-update";
    }

    /**
     * 提交、取消
     * http://localhost:8080/cargo/export/submit.do?id=0 修改状态为1
     * http://localhost:8080/cargo/export/cancel.do?id=0 修改状态为0
     */
    @RequestMapping("/submit")
    public String submit(String id){
        Export export = new Export();
        // 修改条件
        export.setId(id);
        // 修改状态的值
        export.setState(1);
        // 修改 (动态sql)
        exportService.update(export);
        return "redirect:/cargo/export/list";
    }
    @RequestMapping("/cancel")
    public String cancel(String id){
        Export export = new Export();
        // 修改条件
        export.setId(id);
        // 修改状态的值
        export.setState(0);
        // 修改 (动态sql)
        exportService.update(export);
        return "redirect:/cargo/export/list";
    }

    /**
     * 电子报运: 远程访问海关报运平台
     * 1. 先启动jk_export项目
     * 2. 分析访问地址
     *    http://localhost:9001/ws/export/user    POST 传入报运单、报运单的商品信息： ExportVo对象
     *    http://localhost:9001/ws/export/user/1  GET  查询报运结果，修改数据库
     */
    @RequestMapping("/exportE")
    public String exportE(String id){ // id 报运单id
        /* 创建ExportVo，封装WebService请求的数据 */
        // 根据报运单id查询
        Export export = exportService.findById(id);
        // 创建封装WebService请求数据的对象
        ExportVo exportVo = new ExportVo();
        // 对象拷贝 (注意：引入spring包)
        BeanUtils.copyProperties(export,exportVo);
        // 设置报运单id
        exportVo.setExportId(id);

        // 封装报运的商品
        List<ExportProductVo> products = exportVo.getProducts();
        // 根据报运单id，查询商品
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(example);
        // 遍历查询的结果
        if (exportProducts != null && exportProducts.size()>0){
            for (ExportProduct exportProduct : exportProducts) {
                // 创建WebService请求的报运单的商品vo对象
                ExportProductVo vo = new ExportProductVo();
                // 对象拷贝
                BeanUtils.copyProperties(exportProduct,vo);
                // 设置报运单id
                vo.setExportId(id);
                // 设置报运单的商品id
                vo.setExportProductId(exportProduct.getId());
                // 把封装的vo对象添加到集合
                products.add(vo);
            }
        }
        //exportVo.setProducts(products);


        // 电子报运（1）远程访问海关报运平台，录入报运单信息到海关表
        WebClient.create("http://localhost:9001/ws/export/user").post(exportVo);

        // 电子报运（2）远程访问海关报运平台，查询报运结果
        ExportResult exportResult =
                WebClient.create("http://localhost:9001/ws/export/user/" + id).get(ExportResult.class);

        // 修改后台系统的数据库：报运状态、备注、商品交税金额
        exportService.updateExport(exportResult);

        return "redirect:/cargo/export/list";
    }

}















package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.util.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    // 注入dubbo接口代理对象 (要提供service接口实现类，dubbo服务类)
    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;

    /**
     * 1. 列表
     * http://localhost:8080/cargo/contractProduct/list.do?contractId=1
     */
    @RequestMapping("/list")
    public String list(String contractId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        //1. 查询货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //2. 根据购销合同id，查询货物
        ContractProductExample cpExample = new ContractProductExample();
        cpExample.createCriteria().andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo =
                contractProductService.findByPage(cpExample, pageNum, pageSize);

        //3. 保存数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("contractId",contractId);

        return "cargo/product/product-list";
    }

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 2. 添加或修改货物
     * 请求的文件：<input type="file"  name="productPhoto" >
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) {
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(contractProduct.getId())) {
            if (productPhoto != null) {
                try {
                    // 调用工具类实现文件上传到七牛云服务器，返回图片的url路径
                    String url = "http://"+fileUploadUtil.upload(productPhoto);
                    contractProduct.setProductImage(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            contractProductService.save(contractProduct);
        } else {
            contractProductService.update(contractProduct);
        }
        //添加成功，重定向到货物列表，注意要传入contractId购销合同id
        return "redirect:/cargo/contractProduct/list?contractId=" + contractProduct.getContractId();
    }

    /**
     * 3. 修改货物（1）进入修改页面
     * http://localhost:8080/cargo/contractProduct/toUpdate.do?id=8
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        // 根据id查询货物
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);

        //查询货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

        return "cargo/product/product-update";
    }

    /**
     * 4. 删除货物
     * 请求地址：http://localhost:8080/cargo/contractProduct/delete.do
     * 请求参数：
     *      id              货物id，用于执行删除
     *      contractId      购销合同合同id，用于跳转到列表（根据购销合同查询货物）
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list?contractId=" + contractId;
    }

    /**
     * 5. 货物上传（1）进入上传页面
     * http://localhost:8080/cargo/contractProduct/toImport.do?contractId=4
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }


    /**
     * 5. 货物上传（2）导入excel
     * <input type="file" name="file">
     */
    @RequestMapping("/import")
    public String importExcel(String contractId, MultipartFile file) throws Exception {
        // 1. 读取excel文件流，创建工作簿(excel在内存中的表示)
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        // 2. 获取工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 3. 获取总行数
        int count = sheet.getPhysicalNumberOfRows();

        // 4. 遍历每一行数据，从第二行开始遍历（因为第一行是标题）
        // 注意：i=1 表示从第二行开始遍历
        Row row = null;
        for (int i = 1; i < count; i++) {
            // 获取每一行
            row = sheet.getRow(i);
            // 把每一行的数据封装为货物对象
            ContractProduct cp = new ContractProduct();
            cp.setFactoryName(row.getCell(1).getStringCellValue());
            cp.setProductNo(row.getCell(2).getStringCellValue());
            cp.setCnumber((int) row.getCell(3).getNumericCellValue());
            cp.setPackingUnit(row.getCell(4).getStringCellValue());
            cp.setLoadingRate(String.valueOf(row.getCell(5).getNumericCellValue()));
            cp.setBoxNum((int) row.getCell(6).getNumericCellValue());
            cp.setPrice(row.getCell(7).getNumericCellValue());
            cp.setProductDesc(row.getCell(8).getStringCellValue());
            cp.setProductRequest(row.getCell(9).getStringCellValue());
            /* 设置购销合同的id */
            cp.setContractId(contractId);
            /* 根据厂家名称，查询获取厂家id*/
            Factory factory = factoryService.findByFactoryName(cp.getFactoryName());
            if (factory != null) {
                cp.setFactoryId(factory.getId());
            }

            // 调用货物service，实现添加货物
            contractProductService.save(cp);
        }

        // 上传成功后，跳转到购销合同列表
        return "redirect:/cargo/contract/list";
    }

}


















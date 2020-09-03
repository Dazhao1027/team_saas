package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.domain.vo.ExportProductResult;
import cn.itcast.domain.vo.ExportResult;
import cn.itcast.service.cargo.ExportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    // 注入购销合同
    @Autowired
    private ContractDao contractDao;
    // 注入购销合同的货物
    @Autowired
    private ContractProductDao contractProductDao;
    // 注入购销合同的附件
    @Autowired
    private ExtCproductDao extCproductDao;


    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //1. 设置参数：uuid、合同号、修改购销合同的状态
        export.setId(UUID.randomUUID().toString());
        //1.1 获取购销合同id
        String contractIds = export.getContractIds();
        //1.2 分割字符串、遍历
        String[] array = contractIds.split(",");
        // 保存合同号
        String customContract = "";
        for (String contractId : array) {
            // 根据购销合同id查询
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            // 累加合同号，空格隔开
            customContract += contract.getContractNo()+" ";
            // 修改购销合同状态(已生成报运)
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
        // 设置报运单的购销合同的合同号
        export.setCustomerContract(customContract);

        // 构造购销合同id的list集合: 数组-->List
        List<String> contractIdList = Arrays.asList(array);
        // 定义map保存货物id、商品id。在保存附件时候要根据货物id获取商品id
        Map<String,String> map = new HashMap<>();

        //【2. 添加商品】
        //2.1 根据购销合同ids查询购销合同的货物
        // select * from co_contract_product where contract_id in ('1','1')
        ContractProductExample cpExample = new ContractProductExample();
        cpExample.createCriteria().andContractIdIn(contractIdList);
        List<ContractProduct> cpList = contractProductDao.selectByExample(cpExample);

        //2.2 遍历购销合同货物
        for (ContractProduct contractProduct : cpList) {
            // 2.3 创建商品对象、封装商品对象
            ExportProduct exportProduct = new ExportProduct();
            // 对象拷贝 (属性名称一样就拷贝，否则不拷贝保持默认)
            // 导入的包： org.springframework.beans
            // 参数1: source 数据源，有数据的对象
            // 参数2：target 要拷贝到的目标对象
            BeanUtils.copyProperties(contractProduct,exportProduct);
            // 设置id
            exportProduct.setId(UUID.randomUUID().toString());
            // 设置报运单id
            exportProduct.setExportId(export.getId());

            // 2.4 保存商品
            exportProductDao.insertSelective(exportProduct);

            // 用map存储货物id、商品id
            map.put(contractProduct.getId(),exportProduct.getId());
        }

        //【3. 添加商品附件】
        // 3.1 根据购销合同ids查询购销合同的附件
        ExtCproductExample example = new ExtCproductExample();
        example.createCriteria().andContractIdIn(contractIdList);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);

        //3.2 遍历购销合同的附件
        for (ExtCproduct extCproduct : extCproductList) {
            //3.3  创建商品附件对象、封装商品附件
            ExtEproduct extEproduct = new ExtEproduct();
            // 对象拷贝
            BeanUtils.copyProperties(extCproduct,extEproduct);
            // 设置附件id
            extEproduct.setId(UUID.randomUUID().toString());
            // 设置附件关联的报运单
            extEproduct.setExportId(export.getId());
            // 设置商品id;
            // 已知条件：货物id  extCproduct.getContractProductId()
            // 需求拿到：商品id？
            // 根据货物id，获取商品id
            String exportProductId = map.get(extCproduct.getContractProductId());
            extEproduct.setExportProductId(exportProductId);

            //3.4 保存报运单商品附件
            extEproductDao.insertSelective(extEproduct);
        }

        //【4. 添加报运单】
        export.setState(0);
        export.setProNum(cpList.size());
        export.setExtNum(extCproductList.size());
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        //1. 修改报运单
        exportDao.updateByPrimaryKeySelective(export);

        //2. 修改报运单的多个商品
        //2.1 获取商品
        List<ExportProduct> list = export.getExportProducts();
        if (list != null && list.size()>0){
            for (ExportProduct exportProduct : list) {
                // 2.2 调用商品的dao，实现修改
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    // 根据报运的结果，修改数据库：报运状态、备注、交税金额
    @Override
    public void updateExport(ExportResult exportResult) {
        //1. 修改报运单：报运状态、备注
        //1.1 获取报运单id
        String exportId = exportResult.getExportId();
        //1.2 创建报运单对象
        Export export = new Export();
        // 修改条件：id 主键
        export.setId(exportResult.getExportId());
        // 修改的值:state、remark
        export.setState(exportResult.getState());
        export.setRemark(exportResult.getRemark());
        //1.3 修改报运单
        exportDao.updateByPrimaryKeySelective(export);

        //2. 修改商品：交税金额
        Set<ExportProductResult> products = exportResult.getProducts();
        if (products != null && products.size()>0){
            for (ExportProductResult product : products) {
                // 创建商品对象并封装
                ExportProduct exportProduct = new ExportProduct();
                exportProduct.setId(product.getExportProductId());
                exportProduct.setTax(product.getTax());
                // 修改商品：交税金额
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }
}

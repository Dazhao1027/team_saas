package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExportProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// 导入阿里巴巴的包
@Service
public class ContractServiceImpl implements ContractService {

    // 注入dao
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Contract> list = contractDao.selectByExample(contractExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        //1. 设置主键
        contract.setId(UUID.randomUUID().toString());
        //2. 设置总金额默认0
        contract.setTotalAmount(0d);
        //4. 设置购销合同的货物数量、附件数量
        contract.setProNum(0);
        contract.setExtNum(0);
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        //根据合同查询货物
        ContractProductExample example = new ContractProductExample();
        example.createCriteria().andContractIdEqualTo(contract.getId());
        //遍历删除
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(example);
        for (ContractProduct contractProduct : contractProductList) {
            //根据货物查询附件
            List<ExtCproduct> extCproducts = contractProduct.getExtCproducts();
            for (ExtCproduct extCproduct : extCproducts) {
                //根据id删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
            //根据id删除货物
            contractProductDao.deleteByPrimaryKey(contractProduct.getId());
        }
        //根据id删除合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> findByDeptId(String deptId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Contract> list = contractDao.findByDeptId(deptId);
        return new PageInfo<>(list);
    }
}

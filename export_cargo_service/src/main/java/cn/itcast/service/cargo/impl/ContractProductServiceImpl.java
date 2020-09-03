package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.domain.vo.ContractProductVo;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

// 导入阿里巴巴的包
@Service
public class ContractProductServiceImpl implements ContractProductService {

    // 注入dao
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractProductExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ContractProduct contractProduct) {
        //1. 设置id、计算货物金额
        contractProduct.setId(UUID.randomUUID().toString());
        Double amount = 0d;
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        contractProduct.setAmount(amount);

        //2. 修改购销合同：总金额、货物数量
        //2.1 根据购销合同id查询
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //2.2 修改总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //2.2 修改货物数量
        contract.setProNum(contract.getProNum() + 1);
        //2.3 修改
        contractDao.updateByPrimaryKeySelective(contract);

        //3. 添加货物
        contractProductDao.insertSelective(contractProduct);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        // 1. 计算修改后的货物金额
        Double amount = 0d;
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        contractProduct.setAmount(amount);

        // 获取修改前的货物金额（从数据库获取）
        ContractProduct cp = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAmount = cp.getAmount();

        // 2. 修改购销合同
        // 2.1 总金额 = 总金额 + 修改后 - 修改前
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount);
        // 2.2 修改
        contractDao.updateByPrimaryKeySelective(contract);

        // 3. 修改货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
    }

    // 删除货物
    @Override
    public void delete(String id) {
        // 1. 获取货物金额
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double cpAmount = contractProduct.getAmount();

        // 2. 查询货物下的所有附件：获取附件的总金额、删除附件
        ExtCproductExample example = new ExtCproductExample();
        // 查询条件：货物id;     查询结果：附件列表
        example.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);
        Double extAmount = 0d;
        if (extCproductList != null && extCproductList.size() > 0){
            for (ExtCproduct extCproduct : extCproductList) {
                if (extCproduct.getAmount() != null) {
                    extAmount += extCproduct.getAmount();
                }
                // 删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        // 3. 修改购销合同
        // 3.1 总金额 = 总金额 - 货物金额 - 附件金额
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - cpAmount - extAmount);
        // 3.2 货物数量
        contract.setProNum(contract.getProNum() - 1);
        // 3.3 附件数量
        contract.setExtNum(contract.getExtNum() - extCproductList.size());
        // 3.4 修改
        contractDao.updateByPrimaryKeySelective(contract);

        // 4. 删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ContractProductVo> findByShipTime(String companyId, String shipTime) {
        return contractProductDao.findByShipTime(companyId,shipTime);
    }
}

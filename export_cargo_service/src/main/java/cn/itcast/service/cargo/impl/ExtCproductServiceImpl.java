package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

// 导入阿里巴巴的包
@Service(timeout = 300000)
public class ExtCproductServiceImpl implements ExtCproductService {

    // 注入dao
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproductExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 附件添加
     * 1. 计算附件金额
     * 2. 修改购销合同：总金额、附件数量
     * 3. 添加附件
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        //1. 设置id、计算货物金额
        extCproduct.setId(UUID.randomUUID().toString());
        Double amount = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null) {
            amount = extCproduct.getPrice() * extCproduct.getCnumber();
        }
        extCproduct.setAmount(amount);

        //2. 修改购销合同：总金额、附件数量
        //2.1 根据购销合同id查询
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //2.2 修改总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //2.2 修改附件数量
        contract.setExtNum(contract.getExtNum() + 1);
        //2.3 修改
        contractDao.updateByPrimaryKeySelective(contract);

        //3. 添加附件
        extCproductDao.insertSelective(extCproduct);
    }

    /**
     * 1. 修改附件
     * A. 计算附件金额
     * B. 修改购销合同：总金额
     * C. 修改附件
     */
    @Override
    public void update(ExtCproduct extCproduct) {
        // 1. 计算修改后的附件金额
        Double amount = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null) {
            amount = extCproduct.getPrice() * extCproduct.getCnumber();
        }
        extCproduct.setAmount(amount);

        // 获取修改前的附件金额（从数据库获取）
        ExtCproduct extc = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldAmount = extc.getAmount();

        // 2. 修改购销合同
        // 2.1 总金额 = 总金额 + 修改后 - 修改前
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount);
        // 2.2 修改
        contractDao.updateByPrimaryKeySelective(contract);

        // 3. 修改附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
    }

    /**
     * 附件删除
     * 1. 根据附件id查询，货物附件金额
     * 2. 修改购销合同
     *    总金额 = 总金额 - 附件金额
     *    数量减1
     * 3. 删除附件
     */
    @Override
    public void delete(String id) {
        //1. 根据附件id查询，货物附件金额
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();

        //2. 修改购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //2.1 总金额 = 总金额 - 附件金额
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        //2.2 附件数量减1
        contract.setExtNum(contract.getExtNum()-1);
        //2.3 修改
        contractDao.updateByPrimaryKeySelective(contract);

        //3. 删除附件
        extCproductDao.deleteByPrimaryKey(id);
    }
}

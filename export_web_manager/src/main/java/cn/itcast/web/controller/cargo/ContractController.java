package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    // 注入dubbo接口代理对象
    @Reference
    private ContractService contractService;

    /**
     * 1. 列表
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 构造查询条件
        ContractExample example = new ContractExample();
        // 排序：create_time 创建时间降序排序
        example.setOrderByClause("create_time desc");
        ContractExample.Criteria criteria = example.createCriteria();
        // 查询条件：company_id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        /**
         * 细粒度权限控制：不同用户查看购销合同列表看到的数据不一样
         * 1. 登陆用户等级degree=4,普通用户，只能查看自己创建的购销合同
         * 2. 登陆用户等级degree=3,部门经理，可以查看当前部门的所有购销合同
         * 3. 登陆用户等级degree=2,大部门经理，可以查看当前部门的购销合同，以及所有子部门
         */
        User loginUser = getLoginUser();
        PageInfo<Contract> pageInfo = null;
        if (loginUser.getDegree() == 4) {
            // SELECT * FROM co_contract WHERE create_by='登陆用户的id'
            criteria.andCreateByEqualTo(loginUser.getId());
            pageInfo = contractService.findByPage(example, pageNum, pageSize);
        } else if (loginUser.getDegree() == 3) {
            // SELECT * FROM co_contract WHERE create_dept='登陆用户的部门id'
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
            pageInfo = contractService.findByPage(example, pageNum, pageSize);
        } else if (loginUser.getDegree() == 2) {
            // SELECT * FROM co_contract WHERE FIND_IN_SET(create_dept,getDeptChild('100'))
            pageInfo = contractService.findByDeptId(loginUser.getDeptId(), pageNum, pageSize);
        }

        // 保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "cargo/contract/contract-list";
    }

    /**
     * 2. 添加（1）进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 2. 添加（2）添加
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        // 判断:根据主键是否为空，判断是添加还是修改
        if (StringUtils.isEmpty(contract.getId())) {
            // 记录创建时间、创建人
            contract.setCreateTime(new Date());
            // 当前购销合同的创建人就是登陆用户
            contract.setCreateBy(getLoginUser().getId());
            // 设置当前购销合同创建人所属部门
            contract.setCreateDept(getLoginUser().getDeptId());

            // 调用service，添加
            contractService.save(contract);
        } else {
            // 调用service，修改
            contractService.update(contract);
        }
        //添加成功，重定向到列表
        return "redirect:/cargo/contract/list";
    }

    /**
     * 3. 修改 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-update";
    }

    /**
     * 4. 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(String id) {
        final Integer normalState = 0;
        Integer state = contractService.findById(id).getState();
        if (state.equals(normalState)) {
            contractService.delete(id);
            return "ok";
        }
        return "error";
    }

    /**
     * 5. 提交：把购销合同状态改为1
     * http://localhost:8080/cargo/contract/submit.do?id=1
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        // 修改购销合同状态
        Contract contract = new Contract();
        // 修改条件
        contract.setId(id);
        // 修改字段 (动态sql修改, 对象属性有值才生成修改字段)
        contract.setState(1);
        // 修改
        contractService.update(contract);
        return "redirect:/cargo/contract/list";
    }

    /**
     * 5. 取消：把购销合同状态改为0
     * http://localhost:8080/cargo/contract/cancel.do?id=1
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        // 修改购销合同状态
        Contract contract = new Contract();
        // 修改条件
        contract.setId(id);
        // 修改字段 (动态sql修改, 对象属性有值才生成修改字段)
        contract.setState(0);
        // 修改
        contractService.update(contract);
        return "redirect:/cargo/contract/list";
    }

    /**
     * 5. 查看
     * http://localhost:8080/cargo/contract/cancel.do?id=1
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-view";
    }
}















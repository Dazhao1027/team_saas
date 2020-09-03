package cn.itcast.domain.system;

import lombok.Data;

@Data
public class Dept {
    private String id;
    private String deptName;
    private Integer state;
    private String companyId;
    private String companyName;
    // 封装当前部门关联的上级部门  association
    private Dept parent;

}

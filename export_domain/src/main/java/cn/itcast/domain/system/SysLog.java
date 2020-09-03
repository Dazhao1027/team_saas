package cn.itcast.domain.system;

import lombok.Data;

import java.util.Date;

@Data
public class SysLog {

    private String id;
    private String userName;
    private String ip;
    private Date time;
    private String method;
    private String action;
    private String companyId;
    private String companyName;
}
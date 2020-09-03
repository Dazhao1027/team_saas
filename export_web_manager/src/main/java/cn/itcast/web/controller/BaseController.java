package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 抽取controller中公用的操作
 */
public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired(required = false)
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;

    /**
     * 获取登陆用户的企业id
     */
    public String getLoginCompanyId(){
        return getLoginUser().getCompanyId();
    }
    /**
     * 获取登陆用户的企业名称
     */
    public String getLoginCompanyName(){
        return getLoginUser().getCompanyName();
    }

    public User getLoginUser(){
        return (User) session.getAttribute("loginUser");
    }
}

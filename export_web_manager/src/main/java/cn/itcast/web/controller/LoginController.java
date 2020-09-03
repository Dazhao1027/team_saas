package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

   /* @RequestMapping("/login")
    public String login(String email,String password){
        // 判断
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            // 跳转到登陆页面
            return "forward:/login.jsp";
        }
        // 调用service，根据email查询
        User user = userService.findByEmail(email);
        if (user != null) { // 用户名存在
            // 判断密码
            if (user.getPassword().equals(password)) {
                *//* 登陆成功 *//*
                // 查询用户的权限（动态菜单显示）
                List<Module> moduleList = moduleService.findModuleByUserId(user.getId());
                session.setAttribute("moduleList",moduleList);

                session.setAttribute("loginUser",user);
                return "home/main";
            }
        }
        // 登陆失败
        request.setAttribute("error","用户名或者密码错误");
        return "forward:/login.jsp";
    }*/

    /* 使用shiro提供的认证 */
    @RequestMapping("/login")
    public String login(String email,String password){
        // 判断
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            // 跳转到登陆页面
            return "forward:/login.jsp";
        }

        try {
            /* shiro 认证 */
            //1. 获取shiro的subject对象，在shiro中表示当前的登陆用户
            Subject subject = SecurityUtils.getSubject();
            //2. 创建token封装账号密码
            AuthenticationToken token = new UsernamePasswordToken(email,password);
            //3. 登陆认证
            subject.login(token);

            //4. 登陆认证成功（0） 先获取登陆用户的身份对象 (realm认证方法返回对象的构造函数的第一个参数)
            User user = (User) subject.getPrincipal();
            //4. 登陆认证成功（1） 保存登陆用户到session
            session.setAttribute("loginUser",user);
            //4. 登陆认证成功（2） 查询用户的权限作为动态菜单数据显示
            List<Module> moduleList = moduleService.findModuleByUserId(user.getId());
            session.setAttribute("moduleList",moduleList);

            return "home/main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            // 登陆失败
            request.setAttribute("error","用户名或者密码错误");
            return "forward:/login.jsp";
        }

    }

    @RequestMapping("/home")
    public String home(){
        // WEB-INF/pages/home/home.jsp
        return "home/home";
    }

    // 注销
//    @RequestMapping("/logout")
//    public String logout(){
//        // 清空shiro的认证信息 (可选)
//        SecurityUtils.getSubject().logout();
//        // 先释放资源
//        session.removeAttribute("loginUser");
//        // 销毁服务器端的session对象
//        session.invalidate();
//        return "forward:/login.jsp";
//    }

}

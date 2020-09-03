package cn.itcast.web.realm;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自定义的realm类：
 * 1. 主要的作用就是访问数据库查询认证授权的数据
 * 2. access  your  security  data   (访问安全管理数据)
 *
 * 常见的错误：
 * UnknownAccountException          用户名错误
 * IncorrectCredentialsException    密码错误
 */
public class AuthRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    // 认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 转换，获取用户输入的用户名
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername();

        //2. 根据email查询数据库
        User user = userService.findByEmail(email);
        if (user == null) {
            // 用户名错误
            return null; // UnknownAccountException
        }

        //3. 用户名存在，获取数据库中正确的密码
        String dbPwd = user.getPassword();

        //4. 返回认证信息对象
        //参数1：认证对象 subject.getPrincipal() 可以获取这里的参数1
        //参数2：数据库中正确的密码（shiro已经拿到了用户输入的密码，校验交给shiro内部完成即可）
        //参数3：realm的名称，可以随意写，getName()获取默认名称
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(user,dbPwd,this.getName());
        return sai;
    }

    // 授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /* 查询登陆用户的权限 */
        //User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 获取用户认证的身份（认证方法返回对象的构造函数的第一个参数）
        User user = (User) principals.getPrimaryPrincipal();

        // 根据用户id查询权限
        List<Module> userModuleList = moduleService.findModuleByUserId(user.getId());

        // 返回权限信息
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        if (userModuleList != null && userModuleList.size()>0){
            for (Module module : userModuleList) {
                // 添加权限信息
                sai.addStringPermission(module.getName());
            }
        }
        return sai;
    }
}














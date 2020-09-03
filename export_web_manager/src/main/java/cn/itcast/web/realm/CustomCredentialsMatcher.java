package cn.itcast.web.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.StringUtils;

/**
 * 自定义的凭证匹配器，自定义加密算法：
 * 1. email作为盐，进行md5加密
 * 2. 注意：数据库的密码，也要按照这里相同的算法进行加密
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{

    /**
     * 指定密码的加密方式、校验规则。
     * @param token 封装用户输入的账号密码信息
     * @param info  认证的身份信息，也就是数据库中的账号密码信息(认证方法的返回结果)
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1. 获取用户输入的email
        String email = (String) token.getPrincipal();

        //2. 获取用户输入的密码
        String inputPassword = new String((char[]) token.getCredentials());

        //3. 对用户输入的密码加密、加盐
        // 参数1： 要加密的密码;  参数2： 盐
        String encodePwd = new Md5Hash(inputPassword,email).toString();

        //4. 获取数据库中的正确的密码
        String dbPassword = (String) info.getCredentials();

        //5. 对比： 用户输入的密码后的密码与数据库的密码进行对比
        return encodePwd.equals(dbPassword);
    }
}

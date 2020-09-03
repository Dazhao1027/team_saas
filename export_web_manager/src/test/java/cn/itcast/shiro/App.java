package cn.itcast.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.junit.Test;

import java.util.UUID;

public class App {
    // shiro提供的md5加密
    // 明文        密文    (彩虹表，撞库)
    //  1      c4ca4238a0b923820dcc509a6f75849b
    @Test
    public void md5() {
        String encododePwd  = new Md5Hash("1").toString();
        System.out.println(encododePwd);
    }
    // 加密，加盐
    @Test
    public void md5Salt() {
        String salt = "lw@export.com";
        String encododePwd  = new Md5Hash("1",salt).toString();
        System.out.println(encododePwd);
    }
    // 加密，加盐,加迭代次数
    @Test
    public void md5SaltIteration() {
        String salt = "lw@export.com";
        String encododePwd  = new Md5Hash("1",salt,2).toString();
        System.out.println(encododePwd);
    }

    // 加密，加盐,加随机盐
    @Test
    public void md5SaltRandom() {
        String salt = UUID.randomUUID().toString();
        System.out.println("随机盐：" + salt);
        String encododePwd  = new Md5Hash("1",salt).toString();
        System.out.println(encododePwd);
    }

    @Test
    public void test() {
        System.out.println(new Sha1Hash("1").toString()); // 40
        System.out.println(new Sha256Hash("1").toString());//64
        System.out.println(new Sha512Hash("1").toString());//128
    }
}

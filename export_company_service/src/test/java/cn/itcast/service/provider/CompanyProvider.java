package cn.itcast.service.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CompanyProvider {
    public static void main(String[] args) throws IOException {
        // 注意：这里一定要写classpath*
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        System.in.read();
    }
}

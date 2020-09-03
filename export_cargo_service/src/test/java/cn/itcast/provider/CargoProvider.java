package cn.itcast.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CargoProvider {
    public static void main(String[] args) throws IOException {
        ApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        System.in.read();
    }
}

package cn.itcast.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext-rabbitmq-consumer.xml");
        System.in.read();
    }
}
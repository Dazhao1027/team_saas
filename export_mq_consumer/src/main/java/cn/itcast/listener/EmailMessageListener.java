package cn.itcast.listener;

import cn.itcast.util.MailUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailMessageListener implements MessageListener {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        try {
            JsonNode jsonNode = MAPPER.readTree(message.getBody());
            String title = jsonNode.get("title").asText();
            String email = jsonNode.get("email").asText();
            String content = jsonNode.get("content").asText();

            System.out.println("标题：" + title);
            System.out.println("邮箱：" + email);
            System.out.println("内容：" + content);

            // 处理发送邮件的业务
            MailUtil.sendMsg(email,title,content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
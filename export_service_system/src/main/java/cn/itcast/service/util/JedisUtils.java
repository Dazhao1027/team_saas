package cn.itcast.service.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * @author 黑马程序员
 */
public class JedisUtils {

    //定义全局唯一的连接池对象
    private static JedisPool jedisPool;

    static {

        //1.解析jedis.properties文件获取里面的数据
        //ResourceBundle: 专业用于解析properties文件的
        //  特点:根据类路径下面的properties文件名就可以解析，不用写文件扩展名
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jedis");//解析配置文件：jedis.properties
        int maxTotal = Integer.parseInt(resourceBundle.getString("maxTotal"));
        int maxWaitMillis = Integer.parseInt(resourceBundle.getString("maxWaitMillis"));
        String host = resourceBundle.getString("host");
        int port = Integer.parseInt(resourceBundle.getString("port"));

        //2.创建连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);     //最大连接数
        config.setMaxWaitMillis(maxWaitMillis); //最大等待连接的超时时间，单位毫秒

        //3.初始化连接池对象
        jedisPool = new JedisPool(config,host,port);
    }

    //对外提供获取连接对象
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}

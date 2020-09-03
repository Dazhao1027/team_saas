package cn.itcast.web.task;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时执行的任务类
 */
public class MyTask {

    /**
     * 每5秒执行一次方法
     */
    public void execute(){
        System.out.println("定时执行："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}

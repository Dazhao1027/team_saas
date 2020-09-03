package cn.itcast.web.exception;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义全局异常，自动捕获控制器的方法
 */
@Component
@Log4j
public class CustomException implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        // 记录错误到日志文件
        log.error("系统出现异常！",ex);
        // 打印异常到控制台
        ex.printStackTrace();

        ModelAndView mv = new ModelAndView();
        // 判断：指定的异常跳到指定的错误页面
        if (ex instanceof UnauthorizedException) {
            mv.setViewName("unauthorized");
        }
        else {
            mv.setViewName("error");
        }
        mv.addObject("errorMsg",ex.getMessage());
        return mv;
    }
}

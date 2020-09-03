package cn.itcast.web.aspect;
import java.util.Date;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动记录日志的切面类
 */
@Component
@Aspect // 指定当前类为切面类（封装了重复执行的代码）
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 在执行目标对象方法之后自动记录日志
     * @param pjp
     * @return
     */
    // 需求：访问日志列表不需要记录日志。
    @Around("execution(* cn.itcast.web.controller..*.*(..)) && !bean(sysLogController)")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            // 放行，执行目标对象方法
            Object retV = pjp.proceed();

            /* 记录日志 */
            SysLog log = new SysLog();
            log.setTime(new Date());
            log.setIp(request.getRemoteAddr());
            // 获取当前执行的方法，并设置到日志对象中
            log.setMethod(pjp.getSignature().getName());
            // 获取当前执行的目标对象全名，并设置到日志对象中
            log.setAction(pjp.getTarget().getClass().getName());
            // 获取登陆用户
            User user = (User) request.getSession().getAttribute("loginUser");
            if (user != null) {
                log.setUserName(user.getUserName());
                log.setCompanyId(user.getCompanyId());
                log.setCompanyName(user.getCompanyName());
            }
            // 保存
            sysLogService.save(log);
            return retV;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    }
}

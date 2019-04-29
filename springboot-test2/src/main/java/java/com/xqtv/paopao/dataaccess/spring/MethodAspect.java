/**
 * 
 */
package java.com.xqtv.paopao.dataaccess.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.com.xqtv.paopao.dataaccess.log.BCLogger;

/**
 * @author Richard
 */
@Component
@Aspect
public class MethodAspect {

    @Pointcut("execution(* com.xqtv.paopao.dataaccess.service..*.*(..))")
    public void aspect() {
        // do nothing
    }

    @Around("aspect()")
    public Object around(JoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return ((ProceedingJoinPoint) joinPoint).proceed();
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time >= 1000) {
                String name = joinPoint.getSignature().getName();
                BCLogger.error(joinPoint.getSignature().getDeclaringType(), "Service method " + name + " used:" + time);
            }
        }
    }
}

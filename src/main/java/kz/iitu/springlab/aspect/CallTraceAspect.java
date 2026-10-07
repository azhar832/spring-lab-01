package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(4)
public class CallTraceAspect {

    private static final Logger log = LoggerFactory.getLogger(CallTraceAspect.class);
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void enter(JoinPoint jp) {
        int d = depth.get();
        log.info("[TRACE] {}--> {}", "  ".repeat(d), jp.getSignature().toShortString());
        depth.set(d + 1);
    }

    @After("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void exit(JoinPoint jp) {
        int d = Math.max(0, depth.get() - 1);
        log.info("[TRACE] {}<-- {}", "  ".repeat(d), jp.getSignature().toShortString());
        if (d == 0) {
            depth.remove();
        } else {
            depth.set(d);
        }
    }
}

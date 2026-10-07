package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallTraceAspect {

    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    @Before("execution(* kz.iitu.springlab.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        int currentDepth = depth.get();
        String indent = "  ".repeat(currentDepth);
        System.out.println(indent + "--> " + joinPoint.getSignature().toShortString());
        depth.set(currentDepth + 1);
    }

    @After("execution(* kz.iitu.springlab.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        int currentDepth = depth.get() - 1;
        depth.set(Math.max(0, currentDepth));
        String indent = "  ".repeat(currentDepth);
        System.out.println(indent + "<-- " + joinPoint.getSignature().toShortString());
    }
}

package kr.co.parkham.commons.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@within(kr.co.parkham.commons.logging.EnableLog) || @annotation(kr.co.parkham.commons.logging.EnableLog)")
    public Object methodLogging(final ProceedingJoinPoint pjp) throws Throwable {
        log.info("request by {}, args: {} ", pjp.getSignature().getDeclaringType(), pjp.getArgs());
        Object requestResult = pjp.proceed();
        log.info("response {}", requestResult);

        return requestResult;
    }
}

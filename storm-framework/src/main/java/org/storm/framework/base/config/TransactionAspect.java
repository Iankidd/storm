package org.storm.framework.base.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @description 通过AOP切面设置全局事务，拦截service包下面所有方法
 * AOP术语：通知（Advice）、连接点（Joinpoint）、切入点（Pointcut)、切面（Aspect）、目标(Target)、代理(Proxy)、织入（Weaving）
 */
@Aspect
@Component
public class TransactionAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 切面点
     */
    private final String POINT_CUT = "execution(public * org.storm.framework.sys..*.*(..))";

    @Pointcut(POINT_CUT)
    private void pointcut() {

    }

    @Around(value = POINT_CUT)
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            Object obj = proceedingJoinPoint.proceed();
            transactionManager.commit(status);
            return obj;
        } catch (Throwable ex) {
            logger.error("error at " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName() + ": " + ExceptionUtils.getFullStackTrace(ex));
            transactionManager.rollback(status);
        }
        return null;
    }
}

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
    private final String POINT_CUT = "execution(* org.storm..*.*ServiceImpl.add*(..)) "
            + "|| execution(* org.storm..*.*ServiceImpl.save*(..)) "
            + "|| execution(* org.storm..*.*ServiceImpl.update*(..)) "
            + "|| execution(* org.storm..*.*ServiceImpl.delete*(..)) "
            + "|| execution(* org.storm..*.*ServiceImpl.set*(..))";

    @Pointcut(POINT_CUT)
    private void pointcut() {

    }

    @Around(value = POINT_CUT)
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("开启事务管理：" + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName());
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            Object obj = proceedingJoinPoint.proceed();
            transactionManager.commit(status);
            logger.info("进行事务提交");
            return obj;
        } catch (Throwable ex) {
            logger.error("error at " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName() + ": " + ExceptionUtils.getFullStackTrace(ex));
            transactionManager.rollback(status);
            logger.error("进行事务回滚");
            throw new Throwable(ex);
        }
    }
}

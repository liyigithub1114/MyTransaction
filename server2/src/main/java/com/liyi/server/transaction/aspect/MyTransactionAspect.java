package com.liyi.server.transaction.aspect;

import com.liyi.server.transaction.annotation.MyTransactional;
import com.liyi.server.transaction.transactional.MyTransaction;
import com.liyi.server.transaction.transactional.MyTransactionManager;
import com.liyi.server.transaction.transactional.TransactionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MyTransactionAspect implements Ordered {


    @Around("@annotation(com.liyi.server.transaction.annotation.MyTransactional)")
    public void invoke(ProceedingJoinPoint point) {
        // 打印出这个注解所对应的方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        MyTransactional myAnnotation = method.getAnnotation(MyTransactional.class);

        String groupId = "";
        if (myAnnotation.isStart()) {
            groupId = MyTransactionManager.createMyTransactionGroup();
        } else {
            groupId = MyTransactionManager.getCurrentGroupId();
        }

        MyTransaction myTransaction = MyTransactionManager.createMyTransaction(groupId);

        try {
            // spring会开启mysql事务
            point.proceed();
            MyTransactionManager.addMyTransaction(myTransaction, myAnnotation.isEnd(), TransactionType.commit);
        } catch (Exception e) {
            MyTransactionManager.addMyTransaction(myTransaction, myAnnotation.isEnd(), TransactionType.rollback);
            e.printStackTrace();
        } catch (Throwable throwable) {
            MyTransactionManager.addMyTransaction(myTransaction, myAnnotation.isEnd(), TransactionType.rollback);
            throwable.printStackTrace();
        }
    }


    @Override
    public int getOrder() {
        return 10000;
    }
}

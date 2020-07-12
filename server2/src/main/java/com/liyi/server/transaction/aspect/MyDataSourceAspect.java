package com.liyi.server.transaction.aspect;

import com.liyi.server.transaction.connection.MyConnection;
import com.liyi.server.transaction.transactional.MyTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class MyDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) throws Throwable {
        if (MyTransactionManager.getCurrent() != null) {
            return new MyConnection((Connection) point.proceed(), MyTransactionManager.getCurrent());
        } else {
            return (Connection) point.proceed();
        }
    }
}

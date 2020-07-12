package com.liyi.server.transaction.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTransactional {
    // 代表属于分布式事务


    boolean isStart() default false;
    boolean isEnd() default false;
}

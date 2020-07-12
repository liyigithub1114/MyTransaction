package com.liyi.server;

import com.liyi.server.transaction.annotation.MyTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    @MyTransactional(isEnd = true)
    @Transactional
    public void test() {
        demoDao.insert("server2");
    }

}

package com.liyi.server.transaction.transactional;

import com.alibaba.fastjson.JSONObject;
import com.liyi.server.transaction.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyTransactionManager {


    private static NettyClient nettyClient;

    private static ThreadLocal<MyTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        MyTransactionManager.nettyClient = nettyClient;
    }

    public static Map<String, MyTransaction> MY_TRANSACION_MAP = new HashMap<>();

    /**
     * 创建事务组，并且返回groupId
     * @return
     */
    public static String createMyTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("command", "create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");

        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建分布式事务
     * @param groupId
     * @return
     */
    public static MyTransaction createMyTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        MyTransaction myTransaction = new MyTransaction(groupId, transactionId);
        MY_TRANSACION_MAP.put(groupId, myTransaction);
        current.set(myTransaction);
        addTransactionCount();

        System.out.println("创建事务");

        return myTransaction;
    }

    /**
     * 添加事务到事务组
     * @param myTransaction
     * @param isEnd
     * @param transactionType
     * @return
     */
    public static MyTransaction addMyTransaction(MyTransaction myTransaction, Boolean isEnd, TransactionType transactionType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", myTransaction.getGroupId());
        jsonObject.put("transactionId", myTransaction.getTransactionId());
        jsonObject.put("transactionType", transactionType);
        jsonObject.put("command", "add");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", MyTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return myTransaction;
    }

    public static MyTransaction getMyTransaction(String groupId) {
        return MY_TRANSACION_MAP.get(groupId);
    }

    public static MyTransaction getCurrent() {
        return current.get();
    }
    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }
}

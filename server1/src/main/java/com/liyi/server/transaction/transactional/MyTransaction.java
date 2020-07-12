package com.liyi.server.transaction.transactional;

import com.liyi.server.transaction.util.Task;

public class MyTransaction {

    private String groupId;
    private String transactionId;
    private TransactionType transactionType;   // commit-待提交，rollback-待回滚
    private Task task = new Task();

    public MyTransaction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.task = new Task();
    }

    public MyTransaction(String groupId, String transactionId, TransactionType transactionType) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
    }

    public Task getTask() {
        return task;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}

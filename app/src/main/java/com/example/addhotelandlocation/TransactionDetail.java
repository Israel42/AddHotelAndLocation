package com.example.addhotelandlocation;

public class TransactionDetail {
    String transaction;

    public TransactionDetail(String transaction) {
        this.transaction = transaction;
    }

    public TransactionDetail() {
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}

package com.starlingbank;

import java.math.BigInteger;

/**
 * Transaction feed of customer account
 * @author Zoey Luu
 */
class Transaction {
    private String feedItemUID;
    String direction;
    String status;
    private String settlementTime;
    private BigInteger amount;
    private BigInteger roundUp;

    /**
     * Constructor
     * @param feedItemUID ID of the transaction feed item
     * @param direction direction of the transaction
     * @param status settlement time of the transaction
     * @param amount amount of the transaction in pence
     */
    Transaction(String feedItemUID, String direction, String status, BigInteger amount) {
        this.feedItemUID = feedItemUID;
        this.direction = direction;
        this.status = status;
        this.amount = amount;
    }

    String getFeedItemUID() {
        return feedItemUID;
    }

    String getDirection() {
        return direction;
    }

    String getStatus() {
        return status;
    }

    void setSettlementTime(String settlementTime) {
        this.settlementTime = settlementTime;
    }

    String getSettlementTime() {
        return settlementTime;
    }

    BigInteger getAmount() {
        return amount;
    }

    BigInteger getRoundUp() {
        return roundUp;
    }

    void setRoundUp(BigInteger roundUp) {
        this.roundUp = roundUp;
    }
}

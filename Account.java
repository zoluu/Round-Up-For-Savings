package com.starlingbank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Account information
 * @author Zoey Luu
 */
class Account {
    private String accountUid;
    private String defaultCategory;
    private String currency;
    private String createdAt;
    private List<Transaction> transactions;

    /**
     * Constructor
     * @param accountUid ID of the account
     * @param defaultCategory default category
     * @param currency currency of the account
     * @param createdAt when the account was created
     * @param authID the access token
     * @param userID the user-agent name
     */
    Account(String accountUid, String defaultCategory, String currency, String createdAt, String authID, String userID) {
        this.accountUid = accountUid;
        this.defaultCategory = defaultCategory;
        this.currency = currency;
        this.createdAt = createdAt;
        this.transactions = getTransactionFeed(authID, userID);
    }

    /**
     * fetches the transaction feed from an account
     * @param authID the access token
     * @param userID the user-agent name
     * @return the transaction feed as a list
     */
    List<Transaction> getTransactionFeed(String authID, String userID){
        List<Transaction> transactionList = new ArrayList<>();
        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/feed/account/" + this.accountUid + "/category/" + this.defaultCategory);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", authID);
            connection.setRequestProperty("User-Agent", userID);
            connection.setRequestMethod("GET");

            if(connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code: " + connection.getResponseCode());
            }

            Scanner scan = new Scanner(connection.getInputStream());
            StringBuilder str = new StringBuilder();
            while(scan.hasNext()) {
                str.append(scan.nextLine());
            }
            scan.close();
            JSONObject obj = new JSONObject(str.toString());

            JSONArray arr = obj.getJSONArray("feedItems");

            try {
                for (int i = 0; i < arr.length(); i++) {
                    String status = arr.getJSONObject(i).getString("status");
                    String direction = arr.getJSONObject(i).getString("direction");
                    String feedItemUid = arr.getJSONObject(i).getString("feedItemUid");
                    BigInteger amount = BigInteger.valueOf( arr.getJSONObject(i).getJSONObject("amount").getInt("minorUnits"));
                    Transaction transaction = new Transaction(feedItemUid, direction, status, amount);
                    if (status.equals("SETTLED") && direction.equals("OUT")) {
                        String settlementTime = arr.getJSONObject(i).getString("settlementTime");
                        transaction.setSettlementTime(settlementTime);
                    }
                    transactionList.add(transaction);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    List<Transaction> getTransactions(){
        return transactions;
    }

    String getAccountUid() {
        return accountUid;
    }

    String getDefaultCategory() {
        return defaultCategory;
    }

    String getCurrency() {
        return currency;
    }

    String getCreatedAt() {
        return createdAt;
    }

}

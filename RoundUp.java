package com.starlingbank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Calculates the total round-up value for a given week and given account
 * @author Zoey Luu
 */
public class RoundUp {
    private static final String authID = "Bearer "; // Enter "Bearer {yourAccessToken}" here
    private static final String userID = ""; // Enter value here for User-Agent

    public static void main(String[] args) throws IOException {
        Account account = getAccountsInfo();
        assert account != null;
        BigInteger weeklyRoundUp = WeeklySavings.getWeeklyRoundUps(account.getTransactionFeed(authID, userID));
        /*
        String savingsAccInfo = createSavingsAccInfo(account.getAccountUid());
        transferToSavings(weeklyRoundUp, savingsAccInfo, account.getAccountUid());
        */
    }

    /**
     * Fetches account information from API then parses
     * @return Account information
     */
    private static Account getAccountsInfo(){
        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/accounts");

            JSONObject obj = getJsonObject(url);

            try {
                JSONArray arr = obj.getJSONArray("accounts");
                String accountUid = arr.getJSONObject(0).getString("accountUid");
                String defaultCategory = arr.getJSONObject(0).getString("defaultCategory");
                String currency = arr.getJSONObject(0).getString("currency");
                String createdAt = arr.getJSONObject(0).getString("createdAt");
                return new Account(accountUid, defaultCategory, currency, createdAt, authID, userID);
            } catch (JSONException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


/*
    private static String createSavingsAccInfo(String accountUid){
        try {
            URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals");
            JSONObject obj = putJsonObject(url);
            try {
                return obj.getString("savingsGoalUid");
            } catch (JSONException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject putJsonObject(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authID);
        connection.setRequestProperty("User-Agent", userID);
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        JSONObject data = new JSONObject().put("folder",
                new JSONObject().put("name", "test creation folder"));
        wr.write(data.toString());

        return data;
    }
 */

    /**
     * Runs a GET request to retrieve information
     * @param url The url to retrieve information from
     * @return Resource information
     * @throws IOException
     */
    private static JSONObject getJsonObject(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authID);
        connection.setRequestProperty("User-Agent", userID);
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code: " + connection.getResponseCode());
        }

        Scanner scan = new Scanner(connection.getInputStream());
        StringBuilder str = new StringBuilder();
        while (scan.hasNext()) {
            str.append(scan.nextLine());
        }
        scan.close();
        return new JSONObject(str.toString());
    }

/*
    private static void transferToSavings(BigInteger amount, String savingAccUid, String accountUid) throws IOException {
        URL url = new URL("https://api-sandbox.starlingbank.com/api/v2/account/a7a08af3-9239-44ee-ad13-1ad7b1906a60/savings-goals");
        putJsonObject(url);
    }

    private static void updateTransactionFeed(String feedItemUID, String direction, String status, BigInteger amount){

    }
*/

}

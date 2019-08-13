package com.starlingbank;

import org.junit.Test;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class RoundUpTest {

    /**
     * Tests whether findRoundUp finds the correct round up
     */
    @Test
    public void testOne() {
        int expected = WeeklySavings.findRoundUp(BigInteger.valueOf(573)).intValue();
        int actual = BigInteger.valueOf(27).intValue();
        assertEquals(expected, actual);
    }

    /**
     * Tests whether findRoundUp finds the correct round up
     */
    @Test
    public void testTwo() {
        int expected = WeeklySavings.findRoundUp(BigInteger.valueOf(6689)).intValue();
        int actual = BigInteger.valueOf(11).intValue();
        assertEquals(expected, actual);
    }

    /**
     * Tests and makes sure a value of 0 throws IllegalArgumentException if passed through findRoundUp
     */
    @Test(expected = IllegalArgumentException.class)
    public void testThree() {
        int expected = WeeklySavings.findRoundUp(BigInteger.valueOf(0)).intValue();
        int actual = BigInteger.valueOf(0).intValue();
        assertEquals(expected, actual);
    }

    /**
     * Tests what happens when a negative value is passed through findRoundUp
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFour() {
        WeeklySavings.findRoundUp(BigInteger.valueOf(-50));
    }

    /**
     * Tests what happens when null is passed through findRoundUp
     */
    @Test(expected = NullPointerException.class)
    public void testFive() {
        WeeklySavings.findRoundUp(null);
    }

    /**
     * Tests what happens when there is nothing to round up in findRoundUp
     */
    @Test
    public void testSix() {
        int expected = WeeklySavings.findRoundUp(BigInteger.valueOf(500)).intValue();
        int actual = BigInteger.valueOf(0).intValue();
        assertEquals(expected, actual);
    }


    /**
     * Tests whether the check for today is in the current week
     */
    @Test
    public void testSeven() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String timeNow = sdf.format(cal.getTime());
        boolean expected = WeeklySavings.isDateInCurrentWeek(timeNow);
        assertTrue(expected);
    }

    /**
     * Tests whether the check for last weeks date is in the current week
     */
    @Test
    public void testEight() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        cal.add(Calendar.DATE,-7);
        String timeLastWeek = sdf.format(cal.getTime());

        boolean expected = WeeklySavings.isDateInCurrentWeek(timeLastWeek);
        assertFalse(expected);
    }

    /**
     * Tests what happens when null is passed through isDateInCurrentWeek
     */
    @Test(expected = NullPointerException.class)
    public void testNine() {
        WeeklySavings.isDateInCurrentWeek(null);
    }

    /**
     * Creates a list of transactions to test the getWeeklyRoundUps
     */
    @Test
    public void testTen() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String timeNow = sdf.format(cal.getTime());

        List<Transaction> transactionList  = new ArrayList<>();
        Transaction transaction = new Transaction("test1","OUT","SETTLED", BigInteger.valueOf(539));
        transaction.setSettlementTime(timeNow);
        transactionList.add(transaction);
        transaction = new Transaction("test2","OUT","SETTLED", BigInteger.valueOf(7789));
        transaction.setSettlementTime(timeNow);
        transactionList.add(transaction);
        transaction = new Transaction("test3","IN","SETTLED", BigInteger.valueOf(455));
        transaction.setSettlementTime(timeNow);
        transactionList.add(transaction);

        BigInteger expected = WeeklySavings.getWeeklyRoundUps(transactionList);
        BigInteger actual = BigInteger.valueOf(72);
        assertEquals(expected, actual);
    }

    /**
     * Creates a list of transactions to test the getWeeklyRoundUps when the values don't need to be rounded up
     */
    @Test
    public void testEleven() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String timeNow = sdf.format(cal.getTime());

        List<Transaction> transactionList  = new ArrayList<>();
        Transaction transaction = new Transaction("test1","OUT","SETTLED", BigInteger.valueOf(500));
        transaction.setSettlementTime(timeNow);
        transactionList.add(transaction);
        transaction = new Transaction("test2","OUT","SETTLED", BigInteger.valueOf(4000));
        transaction.setSettlementTime(timeNow);

        BigInteger expected = WeeklySavings.getWeeklyRoundUps(transactionList);
        BigInteger actual = BigInteger.valueOf(0);
        assertEquals(expected, actual);
    }

    /**
     * Creates a list of transactions to test the getWeeklyRoundUps when there are no transactions made this week
     */
    @Test (expected = NoSuchElementException.class)
    public void testTwelve() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        cal.add(Calendar.DATE,-7);
        String timeLastWeek = sdf.format(cal.getTime());

        List<Transaction> transactionList  = new ArrayList<>();
        Transaction transaction = new Transaction("test1","OUT","SETTLED", BigInteger.valueOf(4000));
        transaction.setSettlementTime(timeLastWeek);
        transactionList.add(transaction);
        transaction = new Transaction("test2","OUT","SETTLED", BigInteger.valueOf(500));
        transaction.setSettlementTime(timeLastWeek);

        BigInteger expected = WeeklySavings.getWeeklyRoundUps(transactionList);
        BigInteger actual = BigInteger.valueOf(0);
        assertEquals(expected, actual);
    }


}
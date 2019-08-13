package com.starlingbank;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Starling Bank round-up savings feature
 * @author Zoey Luu
 */
class WeeklySavings {

    /**
     * Finds the weekly round-up
     * @param outTransactions Amount spent in a transaction that has direction out and has been settled
     * @return Weekly round-up value
     */
    static BigInteger getWeeklyRoundUps(List<Transaction> outTransactions) {
        List<BigInteger> weeklyRoundUp = new ArrayList<>();
        // Find round ups for each transaction
        List<Transaction> collect = outTransactions.stream().filter(t -> t.status.equals("SETTLED")).filter(t -> t.direction.equals("OUT")).collect(Collectors.toList());
        for (Transaction t : collect) {
            if(t.getAmount() == null) throw new NullPointerException("Null value was recorded in transaction feed.");
            if ( isDateInCurrentWeek(t.getSettlementTime()) ) {
                BigInteger amount = t.getAmount();
                t.setRoundUp(findRoundUp(amount));
                weeklyRoundUp.add(t.getRoundUp());
            }
        }
        if (weeklyRoundUp.size() == 0 ) throw new NoSuchElementException("There were no transactions made this week.");
        return weeklyRoundUp.stream().reduce(BigInteger::add).orElseThrow();
    }


    /**
     * Finds the round-up for a transaction
     * @param amountSpent Amount spent in a transaction
     * @return Round-up value
     */
    static BigInteger findRoundUp(BigInteger amountSpent) {
        if(amountSpent == null) throw new NullPointerException("Null value was provided for amount spent.");

        int a = amountSpent.compareTo(BigInteger.ZERO);
        if ( a <= 0) throw new IllegalArgumentException("Amount spent must be greater than 0. Amount spent was : " + amountSpent);

        // check whether amountSpent is already rounded to nearest 100
        BigDecimal quotient = new BigDecimal(amountSpent).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        BigDecimal wholeNum = new BigDecimal(amountSpent.divide(BigInteger.valueOf(100)).multiply(BigInteger.valueOf(100)),2);
        if (quotient.equals(wholeNum)) {
            return BigInteger.ZERO;
        }

        BigInteger ceiling = wholeNum.add(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).toBigInteger();

        return ceiling.subtract(amountSpent);
    }


    /**
     * Finds whether settlementTime is within the current week
     * @param settlementTime Time the transaction was settled
     * @return true if the date is in the current week
     */
    static boolean isDateInCurrentWeek(String settlementTime) {
        if (settlementTime == null) throw new NullPointerException("Null value was provided for settlement time");
        try {
            // convert settlementTime string to simple date format
            TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
            Calendar cal = Calendar.getInstance(timeZone);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setCalendar(cal);
            cal.setTime(sdf.parse(settlementTime));
            int settlementWeek = cal.get(Calendar.WEEK_OF_YEAR);
            int settlementYear = cal.get(Calendar.YEAR);

            // get the current date
            Calendar currentCalendar = Calendar.getInstance();
            int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
            int year = currentCalendar.get(Calendar.YEAR);

            return (week == settlementWeek) && (year == settlementYear);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }


}
package com.tre.centralkitchen.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRangeChecker {
    public static boolean isWithinPastThreeMonths(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date currentDate = new Date();
            Date inputDate = dateFormat.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, -3);
            Date threeMonthsAgo = calendar.getTime();

            return inputDate.after(threeMonthsAgo);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWithinNextMonth(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date currentDate = new Date();
            Date inputDate = dateFormat.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, 1);
            Date oneMonthLater = calendar.getTime();

            return inputDate.before(oneMonthLater);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.androidkudus.lamianota.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.androidkudus.lamianota.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String getFormatDate(Context context, String dateIn){
        @SuppressLint("SimpleDateFormat")DateFormat dateFormatIn = new SimpleDateFormat(context.getString(R.string.date_format_in));
        @SuppressLint("SimpleDateFormat")DateFormat dateFormatOut = new SimpleDateFormat(context.getString(R.string.date_format_out));
        Date date = null;
        if (dateIn == null){
            return null;
        }else {
            try {
                date = dateFormatIn.parse(dateIn);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date != null ? dateFormatOut.format(date) : "";
        }
    }

    public static String getFallbackFormatDate(Context context, String dateIn){
        @SuppressLint("SimpleDateFormat")DateFormat dateFormatIn = new SimpleDateFormat(context.getString(R.string.date_format_out));
        @SuppressLint("SimpleDateFormat")DateFormat dateFormatOut = new SimpleDateFormat(context.getString(R.string.date_format_in));
        Date date = null;
        try {
            date = dateFormatIn.parse(dateIn);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date != null ? dateFormatOut.format(date) : "";
    }
}

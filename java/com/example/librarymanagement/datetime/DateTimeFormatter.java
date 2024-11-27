package com.example.library.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final String ID_CREATION_FORMAT = "ddMMyyHHmmss";
    private static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static String getIdCreationFormat() {
        return ID_CREATION_FORMAT;
    }

    public static String getDefaultDatetimeFormat() {
        return DEFAULT_DATETIME_FORMAT;
    }

    public static String format(LocalDateTime dateTime, String format) {
        if (dateTime == null || format == null || format.isEmpty()) {
            System.err.println("Invalid date or format input.");
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    public static LocalDateTime parse(String dateTimeStr, String format) {
        if (dateTimeStr == null || format == null || format.isEmpty()) {
            throw new IllegalArgumentException("DateTime string or format cannot be null or empty.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    public static String convert(String dateTimeStr, String targetFormat) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(targetFormat);
            return dateTime.format(formatter);
        } catch (Exception e) {
            System.err.println("Failed to convert date: " + e.getMessage());
            return "";
        }
    }

    public static void printCurrentDatetime(String customMessage) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println((customMessage != null ? customMessage : "Current DateTime: ") + now);
    }
}

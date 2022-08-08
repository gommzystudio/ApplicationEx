package de.gommzy.applicationex.utils;

public class TimeUtils {
    public static String timeInMillisToString(long time) {
        final long days = time / 1000 / 60 / 60 / 24;
        final long hours = time / 1000 / 60 / 60 % 24;
        final long minutes = time / 1000 / 60 % 60;
        final long seconds = time / 1000 % 60;
        if (days > 0) {
            return days + "d, " + hours + "h & " + minutes + "m";
        } else if (hours > 0) {
            return hours + "h & " + minutes + "m";
        } else if (minutes > 0) {
            return minutes + "m & " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
}

package de.gommzy.applicationex.groups;

import de.gommzy.applicationex.Applicationex;
import de.gommzy.applicationex.utils.TimeUtils;

import java.util.Calendar;

public class MemberUtils {
    public static String getDuration(long duration) {
        if (duration == -1) {
            return Applicationex.MESSAGES.getMessage("durationselection.lifetime");
        } else {
            return TimeUtils.timeInMillisToString(duration - Calendar.getInstance().getTimeInMillis());
        }
    }
}

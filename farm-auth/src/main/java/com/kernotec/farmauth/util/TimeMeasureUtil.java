package com.kernotec.farmauth.util;

import com.kernotec.farmauth.jpa.enums.TimeMeasureEnum;

public class TimeMeasureUtil {

    public static long getMillisecondsByTypeTime(Integer time, TimeMeasureEnum type) {
        return switch (type) {
            case milliseconds -> Long.valueOf(time);
            case seconds -> getMillisecondsOfSeconds(time);
            case minutes -> getMillisecondsOfMinutes(time);
            case hours -> getMillisecondsOfHours(time);
            case days -> getMillisecondsOfDays(time);
        };
    }

    private static long getMillisecondsOfSeconds(Integer time) {
        return time * 1000L;
    }

    private static long getMillisecondsOfMinutes(Integer time) {
        return getMillisecondsOfSeconds(time) * 60L;
    }

    private static long getMillisecondsOfHours(Integer time) {
        return getMillisecondsOfMinutes(time) * 60L;
    }

    private static long getMillisecondsOfDays(Integer time) {
        return getMillisecondsOfHours(time) * 24L;
    }
}

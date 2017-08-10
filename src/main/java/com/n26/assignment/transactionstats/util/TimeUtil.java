package com.n26.assignment.transactionstats.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

    public static final boolean isTimeOlderThanSixtySeconds(long currentTimeStamp, long timestamp) {
        return (currentTimeStamp - timestamp) / 1000 >= 60;
    }

    public static final int getSecondFromTimeStamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).getSecond();
    }
}

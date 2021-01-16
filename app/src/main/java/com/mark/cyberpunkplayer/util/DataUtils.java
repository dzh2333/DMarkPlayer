package com.mark.cyberpunkplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";

    public static String DateToString(Date source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        return simpleDateFormat.format(source);
    }

}

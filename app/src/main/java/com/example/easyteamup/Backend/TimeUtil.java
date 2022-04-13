package com.example.easyteamup.Backend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class TimeUtil {

    private static String KEY_DATE_FORMAT = "yyyy-MM-dd";
    private static String MILITARY_DATE_FORMAT = "HHmm";

    public static Map<String, List<Map<String, String>>> getAvailability(ArrayList<Date> availDates) {
        Map<String, List<Map<String, String>>> availability = new HashMap<>();
        List<Map<String, String>> valueList = new ArrayList<>();
        for (int i = 0; i < availDates.size() / 2; i += 2) {
            String start = new SimpleDateFormat(MILITARY_DATE_FORMAT).format(availDates.get(i).getTime());
            String end = new SimpleDateFormat(MILITARY_DATE_FORMAT).format(availDates.get(i + 1).getTime());
            String date = new SimpleDateFormat(KEY_DATE_FORMAT).format(availDates.get(i).getTime());
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("start", start);
            valueMap.put("end", end);
            valueList.add(valueMap);
            availability.put(date, valueList);
        }
        return availability;
    }

}

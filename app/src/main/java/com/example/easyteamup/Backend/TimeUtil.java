package com.example.easyteamup.Backend;

import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public static Map<String, List<Map<String, String>>> sendTimes(HashMap<LocalDate, ArrayList<ZonedDateTime>> availTimes){
        Map<String, List<Map<String, String>>> availability = new HashMap<>();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("HHmm");
        for(Map.Entry<LocalDate, ArrayList<ZonedDateTime>> item : availTimes.entrySet()){
            ArrayList<ZonedDateTime> times = item.getValue();
            List<Map<String, String>> valueList = new ArrayList<>();
            String date = item.getKey().toString();
            for(int i = 0; i < times.size(); i++){
                String start = times.get(i).format(form);
                int end = Integer.parseInt(start) + 14;
                Map<String, String> valueMap = new HashMap<>();
                valueMap.put("start", start);
                valueMap.put("end", Integer.toString(end));
                valueList.add(valueMap);
            }
            availability.put(date, valueList);
        }
        return availability;
    }

    public static HashMap<LocalDate, ArrayList<ZonedDateTime>>
        receiveTimes(Map<String, List<Map<String, String>>> availability){

        HashMap<LocalDate, ArrayList<ZonedDateTime>> availTimes = new HashMap<>();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm").withZone(ZoneId.of("UTC"));
        for(Map.Entry<String, List<Map<String, String>>> item : availability.entrySet()){
            LocalDate date = LocalDate.parse(item.getKey(), dateForm);
            ArrayList<ZonedDateTime> times = new ArrayList<>();
            for(int i=0; i < item.getValue().size(); i++){
                String t = item.getValue().get(i).get("start");
                ZonedDateTime time = ZonedDateTime.parse(date + " " + t, timeForm);
                times.add(time);
            }
            availTimes.put(date, times);
        }
        return availTimes;
    }

}

package com.servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class Counter {
    Connection con;
    Config config;

    public Counter(Config inConfig, Connection inCon){
        config = inConfig;
        con = inCon;
    }

    public JSONObject get(String timeString) throws SQLException, ClassNotFoundException {
        Statement stmt = con.createStatement();
        JSONObject optionJSON = new JSONObject();
        JSONObject xAxisJSON = new JSONObject();
        JSONObject yAxisJSON = new JSONObject();
        xAxisJSON.put("type", "category");
        yAxisJSON.put("type", "value");
        optionJSON.put("yAxis", yAxisJSON);
        JSONArray xAxisArray = new JSONArray();
        LocalDateTime end = LocalDateTime.now();
        while (end.getSecond() % config.getTimeStep() != 0){
            end = end.minusSeconds(1);
        }
        LocalDateTime begin = end.minusMinutes(config.getTimeInterval());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter categoryFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (timeString != null){
            DateTimeFormatter queryDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            begin = LocalDateTime.parse(timeString, queryDTF).truncatedTo(ChronoUnit.MINUTES);
            end = LocalDateTime.now();
        }
        for (LocalDateTime time = begin; time.isBefore(end); time = time.plusSeconds(config.getTimeStep())){
            xAxisArray.add(time.format(categoryFormatter));
        }
        xAxisJSON.put("data", xAxisArray);
        optionJSON.put("xAxis", xAxisJSON);
        JSONArray seriesJSON = new JSONArray();
        JSONArray legendJSON = new JSONArray();
        for (String tableName: config.getTableNames()){
            for (String changeTypeName: config.getCategoryNames()){
                String preparedQuery = "SELECT COUNT(*) AS count FROM "+tableName+" WHERE dateAndTime > ? AND dateAndTime <= ? and changeType = '"+changeTypeName+"'";
                PreparedStatement pstmt = con.prepareStatement(preparedQuery);
                String lineName = changeTypeName;
                legendJSON.add(lineName);
                JSONObject yJSON = new JSONObject();
                JSONArray yArray = new JSONArray();
//                System.out.println(tableName);
                for (LocalDateTime time = begin; time.isBefore(end); time = time.plusSeconds(config.getTimeStep())){
                    String from = time.format(formatter);
                    String to = time.plusSeconds(config.getTimeStep()).format(formatter);
                    if (config.getDBTimeZone() != null){
                        ZoneId currentZone = ZoneId.systemDefault();
                        ZoneId zoneId = ZoneId.of(config.getDBTimeZone());
                        from = time.atZone(currentZone).withZoneSameInstant(zoneId).format(formatter);
                        to = time.plusSeconds(config.getTimeStep()).atZone(currentZone).withZoneSameInstant(zoneId).format(formatter);
                    }
                    pstmt.setString(1, from);
                    pstmt.setString(2, to);
//                    System.out.println(tmp);
                    ResultSet rst = pstmt.executeQuery();
                    while (rst.next()){
                        yArray.add(rst.getInt("count"));
                    }
                    yJSON.put("data", yArray);
                    yJSON.put("type", "line");
                    yJSON.put("smooth", true);
                    yJSON.put("name", lineName);
                }
//                optionJSON.put(tableName, yArray);
                seriesJSON.add(yJSON);
            }
        }
        JSONObject legendObject = new JSONObject();
        legendObject.put("data", legendJSON);
        optionJSON.put("legend", legendObject);
        optionJSON.put("series", seriesJSON);
        DateTimeFormatter onlyDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        optionJSON.put("date", LocalDateTime.now().format(onlyDateFormat));
        JSONObject titleObject = new JSONObject();
        titleObject.put("text", config.getTitle());
        optionJSON.put("title", titleObject);
//        System.out.println(optionJSON);
        return optionJSON;
    }
}

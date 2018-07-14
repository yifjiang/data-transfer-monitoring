package com.servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime begin = end.minusMinutes(config.getTimeInterval());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter categoryFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (timeString != null){
            DateTimeFormatter queryDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");//TODO:specify the time format
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
                String lineName = tableName+" : "+changeTypeName;
                legendJSON.add(lineName);
                JSONObject yJSON = new JSONObject();
                JSONArray yArray = new JSONArray();
                System.out.println(tableName);
                for (LocalDateTime time = begin; time.isBefore(end); time = time.plusSeconds(config.getTimeStep())){
                    String tmp = String.format(
                            "SELECT COUNT(*) AS count FROM %s WHERE dateAndTime > '%s' AND dateAndTime <= '%s' and changeType = '%s'",
                            tableName,
                            time.format(formatter),
                            time.plusSeconds(config.getTimeStep()).format(formatter),
                            changeTypeName
                    );
//                    System.out.println(tmp);
                    ResultSet rst = stmt.executeQuery(
                            tmp
                    );
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
        System.out.println(optionJSON);
        return optionJSON;
    }
}

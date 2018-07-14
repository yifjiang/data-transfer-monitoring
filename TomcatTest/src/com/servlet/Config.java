package com.servlet;

public interface Config {
    String getSqlConnectionUrl();

    String getSqlClass();

    String[] getTableNames();

    long getTimeInterval();//in minutes

    long getTimeStep();//in seconds

    String[] getCategoryNames();

    String getDBTimeZone();
}

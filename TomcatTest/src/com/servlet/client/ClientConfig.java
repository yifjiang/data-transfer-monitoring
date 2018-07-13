package com.servlet.client;

import com.servlet.Config;

public class ClientConfig implements Config {
	private static String sqlConnectionUrl = "jdbc:mysql://localhost/testMonitor?" +
            "user=root&password=Vm450Group7&serverTimezone=UTC";

//    private static String[] tableNames = {"receiveCount", "appliedCount"};
    private static String[] tableNames = {"receiveCount"};

    private static long timeInterval = 10;//in minutes

    private static long timeStep = 30;//in seconds

    private static String sqlClass = "com.mysql.cj.jdbc.Driver";

    @Override
    public String getSqlConnectionUrl() {
        return sqlConnectionUrl;
    }

    @Override
    public String getSqlClass() {
        return sqlClass;
    }

    @Override
    public String[] getTableNames() {
        return tableNames;
    }

    @Override
    public long getTimeInterval() {
        return timeInterval;
    }

    @Override
    public long getTimeStep() {
        return timeStep;
    }

    @Override
    public String[] getCategoryNames() {
        return new String[]{"INSERT", "UPDATE", "DELETE"};
    }
}
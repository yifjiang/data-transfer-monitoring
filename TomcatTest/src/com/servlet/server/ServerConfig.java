package com.servlet.server;

import com.servlet.Config;

public class ServerConfig implements Config {
	static String sqlConnectionUrl = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=testMonitor;" +
            "user=sa;password=Vm450Group7;";

    private static String[] tableNames = {"capturedCount"};

    private static long timeInterval = 10;//in minutes

    private static long timeStep = 5;//in seconds

    static String sqlClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

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

    @Override
    public String getDBTimeZone() {
        return "Etc/UTC";
    }
}

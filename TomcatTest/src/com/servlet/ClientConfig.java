package com.servlet;

public class ClientConfig {
	static String sqlConnectionUrl = "jdbc:mysql://localhost/testMonitor?" +
            "user=root&password=Vm450Group7&serverTimezone=UTC";

	static String[] tableNames = {"receiveCount", "appliedCount"};

	static int timeInterval = 10000;

	static int timeStep = 500;

    static String sqlClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String hostAddress = "localhost";
    static int hostPort = 8001;

    static String topicName = "Changes";
}

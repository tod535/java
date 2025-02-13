package io.hexlet.javafxrepair;

import io.hexlet.javafxrepair.dao.BaseDAO;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseManager {
    public static Connection connection;

    private final String databaseServerUrl = "jdbc:postgresql://localhost:5432/";
    private final String databaseName = "java-fx-gia-repair";
    private final String username = "java-fx-gia-repair-username";
    private final String password = "java-fx-gia-repair-password";

    private final Map<String, String> TABLE_NAME_AND_FILE_NAME = new LinkedHashMap<>();

    public DatabaseManager() {
        TABLE_NAME_AND_FILE_NAME.put("users", "inputDataUsers.csv");
        TABLE_NAME_AND_FILE_NAME.put("requests", "inputDataRequests.csv");
        TABLE_NAME_AND_FILE_NAME.put("comments", "inputDataComments.csv");
    }

//    public DatabaseManager createDatabase() {
//        try (var serverConnection = DriverManager.getConnection(databaseServerUrl, username, password)) {
//            String sql = String.format("CREATE DATABASE %s;", databaseName);
//            try (var ps = serverConnection.createStatement()) {
//                ps.executeUpdate(sql);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return this;
//    }

    public DatabaseManager openConnection() {
        String databaseUrl = databaseServerUrl + databaseName;
        try {
            connection = DriverManager.getConnection(databaseUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BaseDAO.connection = connection;
        return this;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialization() {
        createTables();
        importOperation();
    }

    private void createTables() {
        String sql = loadSchema();
        try (var ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadSchema() {
        try (var is = DatabaseManager.class.getClassLoader().getResourceAsStream("init.sql")) {
            try (var r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return r.lines().collect(Collectors.joining());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void importOperation() {
        try {
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            TABLE_NAME_AND_FILE_NAME.forEach((tableName, fileName) -> {
                if (isTableEmpty(tableName)) {
                    try (var is = DatabaseManager.class.getClassLoader().getResourceAsStream(fileName)) {
//                        String columns = getColumnNames(tableName);
                        String sql = String.format(
                                "COPY %s FROM STDIN WITH (DELIMITER ';', NULL 'null', HEADER);",
                                tableName
//                                columns
                        );

                        copyManager.copyIn(sql, is);
                        updateSerialIdVal(tableName);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isTableEmpty(String tableName) {
        String sql = String.format("SELECT COUNT(*) FROM %s;", tableName);
        try (var ps = connection.prepareStatement(sql)) {
            ps.executeQuery();
            var rs = ps.getResultSet();
            if (rs.next()) {
                int count = rs.getInt("COUNT");
                return count == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private void updateSerialIdVal(String tableName) {
        String sql = String.format(
                "SELECT setval(pg_get_serial_sequence('%s', 'id'), MAX(id)) FROM %s",
                tableName,
                tableName
        );
        try (var ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    private String getColumnNames(String tableName) {
//        List<String> columns = new ArrayList<>();
//        try {
//            var ps = connection.getMetaData();
//            var columnNames = ps.getColumns(null, null, tableName, null);
//            while (columnNames.next()) {
//                String column = columnNames.getString("COLUMN_NAME");
//                columns.add(column);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return columns.stream().collect(Collectors.joining(", ", "(", ")"));
//    }
}

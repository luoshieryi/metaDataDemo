package moe.gothiclolita.metadatademo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class TestController {

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/meta_data_test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static final Connection connection;

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }

    @GetMapping("/createTable")
    public String createTable(String tableName) {
        try {
            String sql = "CREATE TABLE " + tableName + " (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY (id))";
            connection.createStatement().execute(sql);
            return "success";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropTable")
    public String dropTable(String tableName) {
        try {
            String sql = "DROP TABLE " + tableName;
            connection.createStatement().execute(sql);
            return "success";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/addColumn")
    public String addColumn(String tableName, String columnName, String columnType) {
        try {
            String sql = "ALTER TABLE " + tableName + " ADD " + columnName + " " + columnType;
            connection.createStatement().execute(sql);
            return "success";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropColumn")
    public String dropColumn(String tableName, String columnName) {
        try {
            String sql = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
            connection.createStatement().execute(sql);
            return "success";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getColumnNames")
    public List<ColumnMetaData> getColumnNamesAndTypes(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT, COLUMN_DEFAULT, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            List<ColumnMetaData> columnMetaDatas = new ArrayList<>(resultSet.getFetchSize());
            while (resultSet.next()) {
                ColumnMetaData columnMetaData = new ColumnMetaData();
                columnMetaData.setName(resultSet.getString("COLUMN_NAME"));
                columnMetaData.setType(resultSet.getString("COLUMN_TYPE"));
                columnMetaData.setComment(resultSet.getString("COLUMN_COMMENT"));
                columnMetaData.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
                columnMetaData.setIsNullable(resultSet.getString("IS_NULLABLE"));
                columnMetaDatas.add(columnMetaData);
            }
            return columnMetaDatas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

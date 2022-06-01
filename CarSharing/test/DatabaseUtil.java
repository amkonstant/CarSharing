import org.hyperskill.hstest.exception.outcomes.WrongAnswer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseUtil {
    private static final String databaseFilePath = "./src/carsharing/db/carsharing";
    private static Connection connection;

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + databaseFilePath);
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {
            System.out.println(ignored.getErrorCode());
            throw new WrongAnswer("Can't connect to the database.");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                System.out.println(ignored.getErrorCode());
                throw new WrongAnswer("Can't close connection to the database.");
            }
            connection = null;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void truncate(String table) {
        String truncateQuery = String.format("SET FOREIGN_KEY_CHECKS = 0;" +
                "TRUNCATE TABLE %s RESTART IDENTITY;" +
                "SET FOREIGN_KEY_CHECKS = 1;", table);
        try (var statement = getConnection().createStatement()) {
            statement.execute(truncateQuery);
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public boolean ifTableExist(String tableName) {
        tableName = tableName.toUpperCase();
        try (var resultSet = executeQuery("SHOW TABLES")) {
            while (resultSet.next()) {
                if (resultSet.getString("TABLE_NAME").equals(tableName)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void ifColumnsExist(String tableName, String[][] columns) {
        try (var resultSet = executeQuery("SHOW COLUMNS FROM " + tableName.toUpperCase())) {
            HashMap<String, String> correctColumns = new HashMap<>();
            for (String[] column : columns) {
                correctColumns.put(column[0], column[1]);
            }

            while (resultSet.next()) {
                String columnName = resultSet.getString("FIELD");
                if (correctColumns.containsKey(columnName)) {
                    if (!resultSet.getString("TYPE").contains(correctColumns.get(columnName))) {
                        throw new WrongAnswer("In the '" + tableName.toUpperCase() + "' table '" + columnName
                                + "' column should be of " + correctColumns.get(columnName) + " type.");
                    }
                    correctColumns.remove(columnName);
                }
            }
            if (!correctColumns.isEmpty()) {
                throw new WrongAnswer("Can't find in '" + tableName.toUpperCase() +
                        "' table the following columns: " + correctColumns);
            }
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void clearCompanyTable() {
        truncate("COMPANY");
    }

    public void clearCarTable() {
        truncate("CAR");
    }

    public void clearCustomerTable() {
        truncate("CUSTOMER");
    }

    public void checkCompanyColumnProperties() {
    }

    public void checkCarColumnProperties() {

    }

    public void checkCustomerColumnProperties() {

    }

    public void checkCompany(String company) {
        try (var stmt = executeQuery("SELECT ID FROM COMPANY WHERE NAME LIKE '%" + company + "%';")) {
            if (!stmt.next()) throw new WrongAnswer("Company " + company + " doesn't exists");
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database \n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void checkCar(String company, String car) {
        try (var stmt = executeQuery("SELECT CAR.ID FROM CAR JOIN COMPANY ON COMPANY.ID = CAR.COMPANY_ID" +
                " WHERE CAR.NAME LIKE '%" + car + "%' AND COMPANY.NAME LIKE '%" + company + "%';")) {
            if (!stmt.next()) throw new WrongAnswer("Car " + company + " / " + car + " doesn't exists");
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database \n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void checkCustomer(String customer, String car) {
        try (var stmt = executeQuery("SELECT CUSTOMER.ID, CAR.NAME FROM CUSTOMER LEFT JOIN CAR ON CAR.ID = CUSTOMER.RENTED_CAR_ID" +
                " WHERE CUSTOMER.NAME LIKE '%" + customer + "%';")) {
            if (!stmt.next()) throw new WrongAnswer("Customer " + customer + " doesn't exist!");
            if (car != null) {
                String carName = stmt.getString("CAR.NAME");
                if (!car.equals(carName))
                    throw new WrongAnswer("Rented car " + customer + " / " + car + " doesn't exist");
            }
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database \n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }
}
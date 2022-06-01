package carsharing.repository;

import carsharing.models.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository extends Repository<Customer> {

    public static final String UPDATE = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?;";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS CUSTOMER( ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR NOT NULL UNIQUE, RENTED_CAR_ID INT REFERENCES CAR(ID));";
    private static final String SQL_SELECT_BY_ID = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?";
    private static final String SQL_SELECT_ALL = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER;";
    private static final String SQL_INSERT = "INSERT INTO CUSTOMER(NAME, RENTED_CAR_ID) VALUES(?, ?);";

    public CustomerRepository(Connection connection) throws SQLException {
        super(connection, SQL_SELECT_BY_ID, SQL_CREATE_TABLE, SQL_SELECT_ALL, SQL_INSERT);
    }

    @Override
    protected Customer toObject(ResultSet rs) throws SQLException {
        return new Customer(rs.getInt(1), rs.getString(2), rs.getObject(3, Integer.class));
    }

    @Override
    protected Object[] toParams(Customer customer) {
        return new Object[]{customer.getName(), customer.getRentedCarId()};
    }

    public void update(Customer customer) {
        try {
            if (update == null) update = prepareStatement(UPDATE);
            update.clearParameters();
            applyParameters(update, customer.getRentedCarId(), customer.getId());
            update.executeUpdate();
        } catch (Exception e) {
            handleException(e);
        }
    }
}

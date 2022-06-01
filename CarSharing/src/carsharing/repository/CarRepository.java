package carsharing.repository;

import carsharing.models.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarRepository extends Repository<Car> {
    public static final String SELECT_ALL_AVAILABLE = "SELECT CAR.ID, CAR.NAME, COMPANY_ID FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID WHERE COMPANY_ID = ? AND CUSTOMER.ID IS NULL;";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS CAR(ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR NOT NULL UNIQUE, COMPANY_ID INT NOT NULL REFERENCES COMPANY(ID));";
    private static final String SQL_SELECT_BY_ID = "SELECT ID, NAME, COMPANY_ID FROM CAR WHERE ID = ?;";
    private static final String SQL_SELECT_ALL = "SELECT ID, NAME, COMPANY_ID FROM CAR WHERE COMPANY_ID = ?;";
    private static final String SQL_INSERT = "INSERT INTO CAR(NAME, COMPANY_ID) VALUES (?, ?);";
    private static final String SQL_UPDATE = "UPDATE CAR SET NAME = ?, COMPANY_ID = ? WHERE ID = ?;";
    private PreparedStatement selectAllAvailable;

    public CarRepository(Connection connection) throws SQLException {
        super(connection, SQL_SELECT_BY_ID, SQL_CREATE_TABLE, SQL_SELECT_ALL, SQL_INSERT);
    }

    public List<Car> selectAllAvailable(int companyId) {
        try {
            if (selectAllAvailable == null) selectAllAvailable = prepareStatement(SELECT_ALL_AVAILABLE);
            applyParameters(selectAllAvailable, companyId);
            ResultSet rs = selectAllAvailable.executeQuery();
            var list = new ArrayList<Car>();
            while (rs.next()) {
                list.add(toObject(rs));
            }
            return list;
        } catch (SQLException e) {
            handleException(e);
        }

        return Collections.emptyList();
    }

    @Override
    protected Car toObject(ResultSet rs) throws SQLException {
        return new Car(rs.getInt(1), rs.getString(2), rs.getInt(3));
    }

    @Override
    protected Object[] toParams(Car car) {
        return new Object[]{car.getName(), car.getCompanyId()};
    }

    @Override
    public void close() throws SQLException {
        super.close();
        var exception = closePreparedStatement(selectAllAvailable);
        if (exception != null) throw exception;
    }
}

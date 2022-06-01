package carsharing.repository;

import carsharing.models.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyRepository extends Repository<Company> {
    public static final String SQL_UPDATE = "UPDATE COMPANY SET NAME = ? WHERE ID = ?;";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS COMPANY (ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR NOT NULL UNIQUE);";
    private static final String SQL_SELECT_BY_ID = "SELECT ID, NAME FROM COMPANY WHERE ID = ?";
    private static final String SQL_SELECT_ALL = "SELECT ID, NAME FROM COMPANY;";
    private static final String SQL_INSERT = "INSERT INTO COMPANY(NAME) VALUES (?);";

    public CompanyRepository(Connection connection) throws SQLException {
        super(connection, SQL_SELECT_BY_ID, SQL_CREATE_TABLE, SQL_SELECT_ALL, SQL_INSERT);
    }

    @Override
    protected Company toObject(ResultSet rs) throws SQLException {
        return new Company(rs.getInt(1), rs.getString(2));
    }

    @Override
    protected Object[] toParams(Company company) {
        return new Object[]{company.getName()};
    }
}

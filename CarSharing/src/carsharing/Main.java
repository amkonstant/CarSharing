package carsharing;

import carsharing.controller.AuthenticationController;
import carsharing.controller.ControllerManager;
import carsharing.repository.CarRepository;
import carsharing.repository.CompanyRepository;
import carsharing.repository.CustomerRepository;
import carsharing.service.CompanyService;
import carsharing.service.CustomerService;
import carsharing.service.ServiceManager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URI = "jdbc:h2:./src/carsharing/db/";

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName(DB_DRIVER);

        try (var connection = DriverManager.getConnection(DB_URI + getDatabaseName(args));
             var companyRepository = new CompanyRepository(connection);
             var carRepository = new CarRepository(connection);
             var customerRepository = new CustomerRepository(connection)) {
            connection.setAutoCommit(true);

            ServiceManager.load(CompanyService.class, new CompanyService(companyRepository, carRepository));
            ServiceManager.load(CustomerService.class, new CustomerService(customerRepository, carRepository));

            Scanner sc = new Scanner(System.in);
            ControllerManager cm = new ControllerManager(new AuthenticationController());

            do {
                if (cm.print()) {
                    String input = sc.nextLine();
                    cm.onInput(input);
                }
            } while (cm.hasMore());

        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static String getDatabaseName(String... args) {
        try {
            return args[Arrays.asList(args).indexOf("-databaseFileName") + 1];
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return "carsharing";
        }
    }

}

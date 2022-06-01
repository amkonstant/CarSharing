package carsharing.controller;

import carsharing.models.Customer;
import carsharing.service.CompanyService;
import carsharing.service.CustomerService;
import carsharing.service.ServiceManager;

public class CustomerController implements Controller {
    private final int customerId;
    private final CompanyService companyService;
    private final CustomerService customerService;

    public CustomerController(Customer customer) {
        this.customerId = customer.getId();
        companyService = ServiceManager.getService(CompanyService.class);
        customerService = ServiceManager.getService(CustomerService.class);
    }

    @Override
    public void print() {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }

    @Override
    public Controller execute(int action) {
        var customer = customerService.findById(customerId);

        switch (action) {
            case 1:
                if (customer.getRentedCarId() != null) return new PrinterController("You've already rented a car!");
                var companies = companyService.getAll();
                if (companies.isEmpty()) return new PrinterController("The company list is empty!");
                return new ListController<>(companies,
                        company -> {
                            var cars = companyService.getAvailableCars(company);
                            if (cars.isEmpty()) {
                                return new PrinterController(String.format("No available cars in the '%s' company%n",
                                        company.getName()));
                            }
                            return new ListController<>(cars,
                                    car -> {
                                        customerService.rentCar(customer, car);
                                        return new PrinterController(String.format("You rented '%s'", car.getName()));
                                    });
                        });
            case 2:
                if (customer.getRentedCarId() == null) return new PrinterController("You didn't rent a car!");
                customerService.returnRentedCar(customer);
                return new PrinterController("You've returned a rented car!");
            case 3:
                if (customer.getRentedCarId() == null) return new PrinterController("You didn't rent a car!");
                var car = customerService.getRentedCar(customer);
                var company = companyService.findById(car.getCompanyId());
                return new PrinterController(
                        String.format("Your rented car:%n%s%nCompany:%n%s", car.getName(), company.getName()));
            default:
                return null;
        }
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }
}

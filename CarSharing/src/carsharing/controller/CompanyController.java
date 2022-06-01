package carsharing.controller;

import carsharing.models.Company;
import carsharing.service.CompanyService;
import carsharing.service.ServiceManager;

public class CompanyController implements Controller {
    private final CompanyService companyService;
    private final Company company;

    public CompanyController(Company company) {
        companyService = ServiceManager.getService(CompanyService.class);
        this.company = company;
    }

    @Override
    public void print() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    @Override
    public Controller execute(int action) {
        switch (action) {
            case 1:
                var cars = companyService.getCars(company);
                if (cars.isEmpty()) return new PrinterController("The car list is empty!");
                return new ListController<>(cars);
            case 2:
                return new CreateController("Enter the car name: ", name -> companyService.addCar(name, company));
            default:
                return null;
        }
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }

}

package carsharing.controller;

import carsharing.service.CompanyService;
import carsharing.service.ServiceManager;

public class ManagerController implements Controller {
    private final CompanyService service = ServiceManager.getService(CompanyService.class);

    @Override
    public void print() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    @Override
    public Controller execute(int action) {
        switch (action) {
            case 1:
                var companies = service.getAll();
                if (companies.isEmpty()) return new PrinterController("The company list is empty");
                return new ListController<>(service.getAll(), CompanyController::new);
            case 2:
                return new CreateController("Enter the company name: ", service::create);
            default:
                return null;
        }
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }
}

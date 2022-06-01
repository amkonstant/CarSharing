package carsharing.controller;

import carsharing.service.CustomerService;
import carsharing.service.ServiceManager;

public class AuthenticationController implements Controller {
    private final CustomerService customerService;

    public AuthenticationController() {
        customerService = ServiceManager.getService(CustomerService.class);
    }

    @Override
    public void print() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    @Override
    public Controller execute(int action) {
        switch (action) {
            case 1:
                return new ManagerController();
            case 2:
                var customers = customerService.getAll();
                if (customers.isEmpty()) return new PrinterController("The customer list is empty!");
                return new ListController<>(customers, CustomerController::new);
            case 3:
                return new CreateController("Enter the customer name: ", customerService::create);
            default:
                return null;
        }
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }

}

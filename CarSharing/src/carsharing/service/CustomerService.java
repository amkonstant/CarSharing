package carsharing.service;

import carsharing.models.Car;
import carsharing.models.Customer;
import carsharing.repository.CarRepository;
import carsharing.repository.CustomerRepository;

import java.util.List;

public class CustomerService implements Service {
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    public CustomerService(CustomerRepository customerRepository, CarRepository carRepository) {
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.selectAll();
    }

    public Customer findById(int id) {
        return customerRepository.selectById(id);
    }

    public void rentCar(Customer customer, Car car) {
        customerRepository.update(new Customer(customer.getId(), customer.getName(), car.getId()));
    }

    public Car getRentedCar(Customer customer) {
        return carRepository.selectById(customer.getRentedCarId());
    }

    public void returnRentedCar(Customer customer) {
        customerRepository.update(new Customer(customer.getId(), customer.getName(), null));
    }

    public void create(String name) {
        customerRepository.insert(new Customer(null, name, null));
    }
}

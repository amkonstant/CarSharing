package carsharing.service;

import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.repository.CarRepository;
import carsharing.repository.Repository;

import java.util.List;

public class CompanyService implements Service {
    private final Repository<Company> companyRepository;
    private final CarRepository carRepository;

    public CompanyService(Repository<Company> companyRepository, CarRepository carRepository) {
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
    }

    public Company findById(int id) {
        return companyRepository.selectById(id);
    }

    public List<Company> getAll() {
        return companyRepository.selectAll();
    }

    public void create(String name) {
        companyRepository.insert(new Company(null, name));
    }

    public List<Car> getCars(Company company) {
        return carRepository.selectAll(company.getId());
    }

    public List<Car> getAvailableCars(Company company) {
        return carRepository.selectAllAvailable(company.getId());
    }

    public void addCar(String name, Company company) {
        carRepository.insert(new Car(null, name, company.getId()));
    }

}

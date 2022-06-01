package carsharing.models;

public class Customer implements Persistable {
    private final Integer id;
    private final String name;
    private final Integer rentedCarId;

    public Customer(Integer id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}

package carsharing.models;

public final class Car implements Persistable {
    private final Integer id;
    private final String name;
    private final Integer companyId;

    public Car(Integer id, String name, Integer companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public Integer getCompanyId() {
        return companyId;
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

package carsharing.models;

public class Company implements Persistable {
    private final Integer id;
    private final String name;

    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
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

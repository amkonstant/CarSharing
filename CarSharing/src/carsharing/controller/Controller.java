package carsharing.controller;

public interface Controller {
    void print();

    Controller execute(int action);

    void execute(String name);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean hasInput() {
        return true;
    }
}

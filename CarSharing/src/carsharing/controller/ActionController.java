package carsharing.controller;

import java.util.function.Supplier;

public class ActionController<T> implements Controller {
    private final String message;

    public ActionController(Supplier<Boolean> fn, String successMsg, String errorMsg) {
        this.message = fn.get() ? successMsg : errorMsg;
    }

    @Override
    public void print() {
        System.out.println(message);
    }

    @Override
    public Controller execute(int action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }
}

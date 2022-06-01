package carsharing.controller;

import java.util.function.Consumer;

public class CreateController implements Controller {
    private final Consumer<String> fn;
    private final String prompt;

    public CreateController(String prompt, Consumer<String> fn) {
        this.prompt = prompt;
        this.fn = fn;
    }

    @Override
    public void print() {
        System.out.println(prompt);
    }

    @Override
    public Controller execute(int action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(String name) {
        fn.accept(name);
    }
}

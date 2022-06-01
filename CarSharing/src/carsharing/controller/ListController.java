package carsharing.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ListController<T> implements Controller {
    private final List<T> list;
    private Function<T, Controller> fn;

    public ListController(List<T> list) {
        this.list = list;
    }

    public ListController(List<T> list, Function<T, Controller> fn) {
        this(list);
        this.fn = fn;
    }

    @Override
    public void print() {
        IntStream.rangeClosed(1, list.size())
                .mapToObj(i -> String.format("%d. %s", i, list.get(i - 1)))
                .forEachOrdered(System.out::println);
        if (hasInput()) {
            System.out.println("0. Back");
        }
    }

    @Override
    public Controller execute(int action) {
        if (fn != null) {
            return fn.apply(list.get(action - 1));
        } else throw new UnsupportedOperationException();
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasInput() {
        return fn != null;
    }
}

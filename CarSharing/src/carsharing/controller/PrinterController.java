package carsharing.controller;

public class PrinterController implements Controller {

    private final String msg;

    public PrinterController(String msg) {
        this.msg = msg;
    }

    @Override
    public void print() {
        System.out.println(msg);
    }

    @Override
    public Controller execute(int action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasInput() {
        return false;
    }
}

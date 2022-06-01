package carsharing.controller;

import java.util.ArrayDeque;
import java.util.Deque;

public class ControllerManager {
    private final Deque<Controller> history;

    public ControllerManager(AuthenticationController initialController) {
        history = new ArrayDeque<>();
        history.add(initialController);
    }

    public boolean print() {
        history.getLast().print();
        if (!history.getLast().hasInput()) {
            goBack();
            return false;
        }
        return true;
    }

    public void onInput(String input) {
        try {
            int action = Integer.parseInt(input);
            if (action == 0) {
                goBack();
            } else {
                var result = history.getLast().execute(action);
                if (result != null) history.add(result);
                else goBack();
            }
        } catch (NumberFormatException e) {
            history.getLast().execute(input);
            goBack();
        }
    }

    public boolean hasMore() {
        return !history.isEmpty();
    }

    private void goBack() {
        history.removeLast();
        while (hasMore() && (
                !history.getLast().hasInput()
                        || history.getLast().getClass().isAssignableFrom(ListController.class))) {
            history.removeLast();
        }
    }
}

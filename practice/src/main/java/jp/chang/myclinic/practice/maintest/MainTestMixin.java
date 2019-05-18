package jp.chang.myclinic.practice.maintest;

import javafx.stage.Window;

import java.util.Optional;
import java.util.function.Supplier;

public interface MainTestMixin {

    default <T> T waitFor(Supplier<Optional<T>> f) {
        int n = 6;
        int timeout = 100;
        for (int i = 0; i < n; i++) {
            Optional<T> t = f.get();
            if (t.isPresent()) {
                return t.get();
            }
            try {
                Thread.sleep(timeout);
                timeout *= 1.5;
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Thread.sleep failed.");
            }
        }
        throw new RuntimeException("waitFor failed");
    }

    default void waitForFail(Supplier<Optional<?>> f) {
        waitFor(() -> {
            if (f.get().isPresent()) {
                return Optional.empty();
            } else {
                return Optional.of(true);
            }
        });
    }

    default <T extends Window> T waitForWindow(Class<T> windowClass) {
        return waitFor(() -> {
            for (Window w : Window.getWindows()) {
                if (windowClass.isInstance(w)) {
                    return Optional.of(windowClass.cast(w));
                }
            }
            return Optional.empty();
        });
    }

}

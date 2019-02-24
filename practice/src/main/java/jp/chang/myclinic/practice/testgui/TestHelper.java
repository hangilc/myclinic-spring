package jp.chang.myclinic.practice.testgui;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface TestHelper {

    default void gui(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Contract("false -> fail")
    default void confirm(boolean value) {
        if (!value) {
            throw new RuntimeException("Test confirmation failed.");
        }
    }

    default <T> T waitFor(Supplier<Optional<T>> f) {
        return waitFor(5, f);
    }

    default <T> T waitFor(int n, Supplier<Optional<T>> f) {
        for (int i = 0; i < n; i++) {
            Optional<T> t = f.get();
            if (t.isPresent()) {
                return t.get();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Thread.sleep failed.");
            }
        }
        throw new RuntimeException("waitFor failed");
    }

    default void waitForTrue(Supplier<Boolean> f ){
        waitForTrue(5, f);
    }

    default void waitForTrue(int n, Supplier<Boolean> f) {
        waitFor(n, () -> Optional.ofNullable(f.get() ? true : null));
    }

    default void waitForFail(int n, Supplier<Optional<?>> f) {
        waitFor(n, () -> {
            if (f.get().isPresent()) {
                return Optional.empty();
            } else {
                return Optional.of(true);
            }
        });
    }

    default <T extends Window> T waitForWindow(Class<T> windowClass) {
        return waitForWindow(5, windowClass);
    }

    default <T extends Window> T waitForWindow(int n, Class<T> windowClass) {
        return waitFor(n, () -> {
            for (Window w : Window.getWindows()) {
                if (windowClass.isInstance(w)) {
                    return Optional.of(windowClass.cast(w));
                }
            }
            return Optional.empty();
        });
    }

    default void waitForWindowDisappear(Window window) {
        waitFor(5, () -> {
            for (Window w : Window.getWindows()) {
                if (w == window) {
                    return Optional.empty();
                }
            }
            return Optional.of(true);
        });
    }

    default <T> T lastElementOf(List<T> list) {
        return list.get(list.size() - 1);
    }

    default MouseEvent createMouseClickedEvent(Node node) {
        Point2D local = new Point2D(2, 2);
        Point2D scene = node.localToScene(local);
        Point2D screen = node.localToScreen(local);
        return new MouseEvent(
                MouseEvent.MOUSE_CLICKED,
                scene.getX(),
                scene.getY(),
                screen.getX(),
                screen.getY(),
                MouseButton.PRIMARY,
                1, // clickCount
                false, // shiftDown
                false, // controlDown
                false, // altDown
                false, // metaDown
                true,  // pi\rimaryButtonDown
                false, // middleButtonDown
                false, // secondaryButtonDown
                true,  // synthesized
                false, // popupTrigger
                false, // stillSincePress
                null   // PickResult
        );

    }
}

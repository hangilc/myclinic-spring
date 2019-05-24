package jp.chang.myclinic.practice.maintest;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    default void waitForTrue(Supplier<Boolean> pred){
        waitFor(() -> {
            if( pred.get() ){
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        });
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

    default void click(Node node){
        MouseEvent evt = createMouseClickedEvent(node);
        node.fireEvent(evt);
    }

    default void confirm(boolean pred){
        confirm(pred, null);
    }

    default void confirm(boolean pred, Runnable detail){
        if( !pred ){
            if( detail != null ) {
                detail.run();
            }
            throw new RuntimeException("Confirmation failed");
        }

    }


}

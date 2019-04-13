package jp.chang.myclinic.practice.componenttest;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface ComponentTestMixin {

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
        return waitFor(8, f);
    }

    default <T> T waitFor(int n, Supplier<Optional<T>> f) {
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

    default void waitForTrue(Supplier<Boolean> f){
        waitForTrue(8, f);
    }

    default void waitForTrue(int n, Supplier<Boolean> f) {
        waitFor(n, () -> Optional.ofNullable(f.get() ? true : null));
    }

    default void waitForFail(Supplier<Optional<?>> f){
        waitForFail(5, f);
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
        waitFor(8, () -> {
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

    default void click(Node node){
        node.fireEvent(createMouseClickedEvent(node));
    }

    default VisitFull2DTO createBlankVisitFull2(int visitId, int patientId, LocalDateTime visitedAt){
        VisitFull2DTO result = new VisitFull2DTO();
        VisitDTO visit = new VisitDTO();
        visit.visitId = visitId;
        visit.patientId = patientId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(visitedAt);
        result.visit = visit;
        result.texts = new ArrayList<>();
        result.hoken = new HokenDTO();
        result.drugs = new ArrayList<>();
        result.shinryouList = new ArrayList<>();
        result.conducts = new ArrayList<>();
        return result;
    }
}

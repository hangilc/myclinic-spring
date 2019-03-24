package jp.chang.myclinic.practice.testintegration;

import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.javafx.Record;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

class IntegrationTestBase {

    MainPane getMainPane() {
        return Context.getInstance().getMainPane();
    }

    void gui(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Contract("false -> fail")
    void confirm(boolean ok) {
        if (!ok) {
            throw new RuntimeException("confirmation failed");
        }
    }

    Record waitForRecord(int visitId) {
        MainPane mainPane = getMainPane();
        return waitFor(10, () -> mainPane.findRecord(visitId));
    }

    void waitForRecordDisappear(Record record){
        waitFor(() -> {
            List<Record> list = getMainPane().listRecord();
            if( list.contains(record) ){
                return Optional.empty();
            } else {
                return Optional.of(true);
            }
        });
    }

    <T extends Window> T waitForWindow(Class<T> windowClass) {
        return waitForWindow(5, windowClass);
    }

    <T extends Window> T waitForWindow(int n, Class<T> windowClass) {
        return waitFor(n, () -> {
            for (Window w : Window.getWindows()) {
                if (windowClass.isInstance(w)) {
                    return Optional.of(windowClass.cast(w));
                }
            }
            return Optional.empty();
        });
    }

    void waitForWindowDisappear(Window window) {
        waitForWindowDisappear(5, window);
    }

    void waitForWindowDisappear(int n, Window window) {
        waitFor(n, () -> {
            for (Window w : Window.getWindows()) {
                if (w == window) {
                    return Optional.empty();
                }
            }
            return Optional.of(true);
        });
    }

    <T> T waitFor(Supplier<Optional<T>> f) {
        return waitFor(5, f);
    }

    <T> T waitFor(int n, Supplier<Optional<T>> f) {
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

    void waitForNot(Supplier<Optional<?>> f) {
        waitFor(() -> {
            Optional<?> opt = f.get();
            if (!opt.isPresent()) {
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        });
    }

    <T> T getLast(List<T> list) {
        return list.get(list.size() - 1);
    }

}

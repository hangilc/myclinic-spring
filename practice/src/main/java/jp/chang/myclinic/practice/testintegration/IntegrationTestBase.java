package jp.chang.myclinic.practice.testintegration;

import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.javafx.Record;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

class IntegrationTestBase {

    MainPane getMainPane() {
        return Globals.getInstance().getMainPane();
    }

    void gui(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Contract("false -> fail")
    void confirm(boolean ok){
        if( !ok ){
            throw new RuntimeException("confirmation failed");
        }
    }

    Record waitForRecord(int visitId) {
        MainPane mainPane = getMainPane();
        return waitFor(10, () -> mainPane.findRecord(visitId));
    }

    int waitForNewSerialId(Supplier<Integer> gen, int lastId){
        return waitFor(10, () -> {
            int id = gen.get();
            if( id > lastId ){
                return Optional.of(id);
            } else {
                return Optional.empty();
            }
        });
    }

    <T extends Window> T waitForWindow(Class<T> windowClass){
        return waitForWindow(5, windowClass);
    }

    <T extends Window> T waitForWindow(int n, Class<T> windowClass){
        return waitFor(n, () -> {
            for(Window w: Window.getWindows()){
                if( windowClass.isInstance(w) ){
                    return Optional.of(windowClass.cast(w));
                }
            }
            return Optional.empty();
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

}

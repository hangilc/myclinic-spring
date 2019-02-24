package jp.chang.myclinic.practice.testgui;

import javafx.application.Platform;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface TestHelper {

    default void gui(Runnable runnable){
        Platform.runLater(runnable);
    }

    @Contract("false -> fail")
    default void confirm(boolean value){
        if( !value ){
            throw new RuntimeException("Test confirmation failed.");
        }
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

    default void waitForTrue(int n, Supplier<Boolean> f){
        waitFor(n, () -> Optional.ofNullable(f.get() ? true : null));
    }

    default void waitForFail(int n, Supplier<Optional<?>> f){
        waitFor(n, () -> {
            if( f.get().isPresent() ){
                return Optional.empty();
            } else {
                return Optional.of(true);
            }
        });
    }

    default <T> T lastElementOf(List<T> list){
        return list.get(list.size() - 1);
    }

}

package jp.chang.myclinic.practice.lib;

import java.util.List;
import java.util.function.Consumer;

public interface Stuffer<T> {
    void stuffInto(T target, Consumer<T> okHandler, Consumer<List<String>> errorHandle);
}

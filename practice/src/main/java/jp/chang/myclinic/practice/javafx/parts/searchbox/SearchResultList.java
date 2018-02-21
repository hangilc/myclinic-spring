package jp.chang.myclinic.practice.javafx.parts.searchbox;

import java.util.List;
import java.util.function.Consumer;

public interface SearchResultList<M> {
    void setResult(List<M> result);
    void setOnSelectCallback(Consumer<M> cb);
}

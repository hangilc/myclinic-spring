package jp.chang.myclinic.practice.javafx.parts.searchboxold;

import java.util.function.Consumer;

public interface SearchResult<M> {

    void search(String text);
    void setOnSelectCallback(Consumer<M> cb);

}

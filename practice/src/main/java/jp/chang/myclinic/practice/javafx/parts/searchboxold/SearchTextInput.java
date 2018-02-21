package jp.chang.myclinic.practice.javafx.parts.searchboxold;

import java.util.function.Consumer;

public interface SearchTextInput {

    void setOnSearchCallback(Consumer<String> cb);

}

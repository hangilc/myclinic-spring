package jp.chang.myclinic.practice.javafx.parts.searchbox;

import java.util.function.Consumer;

public interface SearchTextInput {

    void setOnSearchCallback(Consumer<String> cb);

}

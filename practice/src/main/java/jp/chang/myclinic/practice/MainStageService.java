package jp.chang.myclinic.practice;

import java.util.function.Consumer;

public interface MainStageService {

    void setTitle(String title);
    void setOnTitleChange(Consumer<String> handler);

}

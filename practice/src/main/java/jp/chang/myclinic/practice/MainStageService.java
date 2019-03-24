package jp.chang.myclinic.practice;

import java.util.function.Consumer;

public class MainStageService {

    private Consumer<String> onChangeHandler = s -> {};

    public void setTitle(String title){
        onChangeHandler.accept(title);
    }

    public void onTitleChanged(Consumer<String> handler){
        this.onChangeHandler = handler;
    }

}

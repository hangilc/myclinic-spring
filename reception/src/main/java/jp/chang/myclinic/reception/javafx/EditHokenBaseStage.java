package jp.chang.myclinic.reception.javafx;

import javafx.stage.Stage;

import java.util.function.Consumer;

public class EditHokenBaseStage<T> extends Stage {

    private Consumer<T> processor = System.out::println;

    public void setOnEnter(Consumer<T> consumer){
        this.processor = consumer;
    }

    protected Consumer<T> getEnterProcessor(){
        return this.processor;
    }

}

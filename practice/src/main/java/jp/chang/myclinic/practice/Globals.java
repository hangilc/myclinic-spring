package jp.chang.myclinic.practice;

import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Globals {

    private static Logger logger = LoggerFactory.getLogger(Globals.class);
    private static Globals INSTANCE = new Globals();
    private MainPane mainPane;

    public static Globals getInstance(){
        return INSTANCE;
    }

    private Globals() {

    }

    public MainPane getMainPane() {
        return mainPane;
    }

    void setMainPane(MainPane mainPane) {
        this.mainPane = mainPane;
    }

}

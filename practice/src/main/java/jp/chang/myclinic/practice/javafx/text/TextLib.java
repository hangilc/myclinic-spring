package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.practice.RestService;
import jp.chang.myclinic.practice.javafx.MainPaneService;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenService;

public interface TextLib {

    RestService getRestService();
    MainPaneService getMainPaneService();
    ShohousenService getShohousenLib();

}

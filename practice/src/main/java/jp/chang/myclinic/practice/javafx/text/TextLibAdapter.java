package jp.chang.myclinic.practice.javafx.text;

import jp.chang.myclinic.practice.RestService;
import jp.chang.myclinic.practice.javafx.MainPaneService;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenService;

public class TextLibAdapter implements TextLib {

    @Override
    public RestService getRestService() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public MainPaneService getMainPaneService() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public ShohousenService getShohousenLib() {
        throw new RuntimeException("not implemented");
    }

}

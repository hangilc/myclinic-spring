package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.practice.RestService;
import jp.chang.myclinic.practice.javafx.text.TextLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordLibAdapter implements RecordLib {

    @Override
    public MainPaneService getMainPaneService() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public RestService getRestService() {
        throw new RuntimeException("not implemented");
    }
}

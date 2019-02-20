package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IntegrationTestBase {

    MainPane getMainPane() {
        return Globals.getInstance().getMainPane();
    }

}

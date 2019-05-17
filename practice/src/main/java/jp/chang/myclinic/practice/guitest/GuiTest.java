package jp.chang.myclinic.practice.guitest;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.guitest.mainpane.MainPaneTest;
import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuiTest {

    public static Object[] listTargets(Stage stage, Pane mainPane){
        return new Object[]{
            new MainPaneTest(stage, mainPane)
        };
    }

    private GuiTest() {

    }

}

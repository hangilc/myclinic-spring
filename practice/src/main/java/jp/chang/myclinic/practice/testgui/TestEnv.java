package jp.chang.myclinic.practice.testgui;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.backendasync.BackendAsync;
import jp.chang.myclinic.practice.PracticeConfigService;

public class TestEnv {

    public Stage stage;
    public Pane main;
    public BackendAsync restService;
    public PracticeConfigService configService;

}

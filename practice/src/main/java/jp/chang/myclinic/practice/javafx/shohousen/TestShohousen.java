package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.application.Platform;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.testgui.TestGroup;

public class TestShohousen extends TestGroup {

    {
        addTestProcSingleOnly("disp", this::testDisp);
    }

    private void testDisp(){
        ShohousenPreview.create(ShohousenLibMock.create(), 1, "")
                .thenAcceptAsync(Stage::show, Platform::runLater).join();
    }

}

package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.text.TextDisp;

public class TextDispTest extends ComponentTestBase {

    public TextDispTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private TextDisp createDisp(String content){
        TextDisp disp = new TextDisp(content);
        gui(() -> {
            disp.setPrefWidth(329);
            disp.setPrefHeight(300);
            main.getChildren().setAll(disp);
            stage.sizeToScene();
        });
        return disp;
    }

    @CompTest(excludeFromBatch = true)
    public void testTextDispDisp(){
        createDisp("テスト");
    }

    @CompTest
    public void testTextDispContent(){
        String content = "のどの痛みがある";
        TextDisp disp = createDisp(content);
        confirm(disp.getRep().equals(content));
    }

    @CompTest
    public void testTextDispEmpty(){
        TextDisp disp = createDisp("");
        confirm(disp.getRep().equals("(空白)"));
    }

    @CompTest
    public void testTextDispClick(){
        TextDisp disp = createDisp("");
        class Local {
            private boolean confirmCallback;
        }
        Local local = new Local();
        disp.setOnClickHandler(() -> { local.confirmCallback = true; });
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        waitForTrue(() -> local.confirmCallback);
    }
}

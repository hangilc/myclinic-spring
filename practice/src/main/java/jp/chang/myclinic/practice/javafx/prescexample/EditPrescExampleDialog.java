package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPrescExampleDialog extends PrescExampleBaseDialog {

    private static Logger logger = LoggerFactory.getLogger(EditPrescExampleDialog.class);

    public EditPrescExampleDialog() {
        setTitle("処方例の編集");
    }

    @Override
    Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("適用");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doUpdate());
        closeButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink
        );
        return hbox;
    }

    private void doUpdate(){
        PrescExampleDTO ex = createPrescExample();
        if( ex != null ){
            Service.api.resolveIyakuhinMaster(ex.iyakuhincode, getLocalDate().toString())
                    .thenCompose(master -> {
                        ex.masterValidFrom = master.validFrom;
                        return Service.api.updatePrescExample(ex);
                    })
                    .thenAccept(prescExampleId -> Platform.runLater(this::doClear))
                    .exceptionally(HandlerFX::exceptionally);

        }
    }

}

package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug2.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.drug2.SearchModeChooser;
import jp.chang.myclinic.utilfx.HandlerFX;

public class NewPrescExampleDialog extends PrescExampleBaseDialog {

    //private static Logger logger = LoggerFactory.getLogger(NewPrescExampleDialog.class);

    public NewPrescExampleDialog() {
        super(new SearchModeChooser(DrugSearchMode.Master, DrugSearchMode.Example));
        setTitle("処方例の新規入力");
    }

    Node createCommands() {
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        hbox.getChildren().addAll(
                enterButton,
                cancelButton,
                clearLink
        );
        return hbox;
    }

    private void doEnter() {
        PrescExampleDTO ex = createPrescExample();
        if (ex != null) {
            Service.api.resolveIyakuhinMaster(ex.iyakuhincode, getLocalDate().toString())
                    .thenCompose(master -> {
                        ex.masterValidFrom = master.validFrom;
                        return Service.api.enterPrescExample(ex);
                    })
                    .thenAccept(prescExampleId -> Platform.runLater(this::doClear))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}

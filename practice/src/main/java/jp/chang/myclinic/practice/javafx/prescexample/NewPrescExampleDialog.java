package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug2.DrugData;
import jp.chang.myclinic.practice.javafx.drug2.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.drug2.SearchModeChooser;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;

public class NewPrescExampleDialog extends PrescExampleBaseDialog {

    //private static Logger logger = LoggerFactory.getLogger(NewPrescExampleDialog.class);

    public NewPrescExampleDialog() {
        super(new SearchModeChooser(DrugSearchMode.Master, DrugSearchMode.Example));
        setTitle("処方例の新規入力");
    }

    Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink clearLink = new Hyperlink("クリア");
        Hyperlink currentLink = new Hyperlink("現行");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        currentLink.setOnAction(evt -> doCurrent());
        hbox.getChildren().addAll(
                enterButton,
                cancelButton,
                clearLink,
                currentLink
        );
        return hbox;
    }

    private void doCurrent(){
        int iyakuhincode = getInput().getIyakuhincode();
        if( iyakuhincode == 0 ){
            GuiUtil.alertError("医薬品が選択されていません。");
            return;
        }
        Service.api.resolveIyakuhinMaster(iyakuhincode, LocalDate.now().toString())
                .thenAcceptAsync(master -> {
                    DrugData data = DrugData.fromMaster(master);
                    getInput().setData(data);
                }, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doEnter() {
        PrescExampleDTO ex = createPrescExample();
        if (ex != null) {
            ex.prescExampleId = 0;
            Service.api.enterPrescExample(ex)
                    .thenAccept(prescExampleId -> Platform.runLater(this::doClear))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}

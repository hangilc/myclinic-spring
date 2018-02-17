package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.OptionalWrapper;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConductEditForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductEditForm.class);

    private String at;
    private ConductFullDTO conduct;
    private OptionalWrapper workarea = new OptionalWrapper();
    private VBox shinryouBox = new VBox(4);
    private VBox drugBox = new VBox(4);
    private VBox kizaiBox = new VBox(4);

    public ConductEditForm(ConductFullDTO conduct, String at) {
        super("処置の編集");
        this.at = at;
        this.conduct = conduct;
        getChildren().addAll(
                createTopMenu(),
                workarea,
                createKindInput(conduct.conduct.kind),
                createLabelInput(conduct.gazouLabel),
                createShinryouList(conduct.conductShinryouList),
                createDrugs(conduct.conductDrugs),
                createKizaiList(conduct.conductKizaiList),
                createCommands(conduct)
        );
    }

    private Node createTopMenu(){
        HBox hbox = new HBox(4);
        Hyperlink enterShinryouLink = new Hyperlink("診療行為追加");
        Hyperlink enterDrugLink = new Hyperlink("薬剤追加");
        Hyperlink enterKizaiLink = new Hyperlink("器材追加");
        enterShinryouLink.setOnAction(evt -> doEnterShinryou());
        enterDrugLink.setOnAction(evt -> doEnterDrug());
        hbox.getChildren().addAll(enterShinryouLink, enterDrugLink, enterKizaiLink);
        return hbox;
    }

    private Node createKindInput(int kind){
        HBox hbox = new HBox(4);
        ChoiceBox<ConductKind> choiceBox = new ChoiceBox<>();
        choiceBox.setConverter(new StringConverter<ConductKind>(){
            @Override
            public String toString(ConductKind kind) {
                return kind.getKanjiRep();
            }

            @Override
            public ConductKind fromString(String string) {
                return null;
            }
        });
        choiceBox.getItems().addAll(ConductKind.values());
        choiceBox.getSelectionModel().select(ConductKind.fromCode(kind));
        hbox.getChildren().addAll(new Label("種類："), choiceBox);
        return hbox;
    }

    private Node createLabelInput(GazouLabelDTO gazouLabel){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        String label = gazouLabel == null ? "" : gazouLabel.label;
        Hyperlink editLink = new Hyperlink("編集");
        hbox.getChildren().addAll(new Label("画像ラベル："), new Label(label), editLink);
        return hbox;
    }

    private Node createShinryouList(List<ConductShinryouFullDTO> shinryouList){
        shinryouList.forEach(this::addShinryou);
        return shinryouBox;
    }

    private void addShinryou(ConductShinryouFullDTO shinryou){
        Text label = new Text(shinryou.master.name);
        Hyperlink editLink = new Hyperlink("編集");
        shinryouBox.getChildren().add(new TextFlow(label, editLink));
    }

    private Node createDrugs(List<ConductDrugFullDTO> drugs){
        VBox vbox = new VBox(4);
        drugs.forEach(drug -> {
            Text label = new Text(DrugUtil.conductDrugRep(drug));
            Hyperlink editLink = new Hyperlink("編集");
            vbox.getChildren().add(new TextFlow(label, editLink));
        });
        return vbox;
    }

    private Node createKizaiList(List<ConductKizaiFullDTO> kizaiList){
        VBox vbox = new VBox(4);
        kizaiList.forEach(kizai -> {
            Text label = new Text(KizaiUtil.kizaiRep(kizai));
            Hyperlink editLink = new Hyperlink("編集");
            vbox.getChildren().add(new TextFlow(label, editLink));
        });
        return vbox;
    }

    private Node createCommands(ConductFullDTO conduct){
        HBox hbox = new HBox();
        Button closeButton = new Button("閉じる");
        Hyperlink deleteLink = new Hyperlink("削除");
        closeButton.setOnAction(evt -> onClose(conduct));
        hbox.getChildren().addAll(closeButton, deleteLink);
        return hbox;
    }

    private int getConductId(){
        return conduct.conduct.conductId;
    }

    private void doEnterShinryou(){
        if( workarea.isVisible() ){
            return;
        }
        ConductShinryouForm form = new ConductShinryouForm(at, getConductId()){
            @Override
            protected void onEnter(ConductShinryouDTO shinryou) {
                Service.api.enterConductShinryou(shinryou)
                        .thenCompose(Service.api::getConductShinryouFull)
                        .thenAccept(entered -> Platform.runLater(() -> {
                            workarea.hide();
                            conduct.conductShinryouList.add(entered);
                            addShinryou(entered);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }

            @Override
            protected void onCancel() {
                workarea.hide();
            }
        };
        workarea.show(form);
    }

    private void doEnterDrug(){
        if( workarea.isVisible() ){
            return;
        }
        ConductDrugForm form = new ConductDrugForm(at, getConductId()){

        };
        workarea.show(form);
    }

    protected void onClose(ConductFullDTO conduct){

    }

}

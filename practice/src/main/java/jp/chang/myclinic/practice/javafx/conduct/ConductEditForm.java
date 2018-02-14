package jp.chang.myclinic.practice.javafx.conduct;

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
import jp.chang.myclinic.practice.javafx.parts.OptionalWrapper;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConductEditForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductEditForm.class);

    private OptionalWrapper workarea = new OptionalWrapper();

    public ConductEditForm(ConductFullDTO conduct) {
        super("処置の編集");
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
        VBox vbox = new VBox(4);
        shinryouList.forEach(shinryou -> {
            Text label = new Text(shinryou.master.name);
            Hyperlink editLink = new Hyperlink("編集");
            vbox.getChildren().add(new TextFlow(label, editLink));
        });
        return vbox;
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

    private void doEnterShinryou(){
        ConductShinryouForm form = new ConductShinryouForm();
        workarea.show(form);
    }

    protected void onClose(ConductFullDTO conduct){

    }

}

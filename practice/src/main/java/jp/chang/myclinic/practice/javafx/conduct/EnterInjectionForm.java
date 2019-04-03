package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SearchResult;
import jp.chang.myclinic.practice.javafx.parts.SearchTextBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class EnterInjectionForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(EnterInjectionForm.class);

    private String at;
    private int conductDrugId = 0;
    private int conductId = 0;
    private DrugInput drugInput = new DrugInput();
    private RadioButtonGroup<ConductKind> kindGroup;
    private SearchResult<IyakuhinMasterDTO> searchResult;

    public EnterInjectionForm(String at) {
        super("処置注射入力");
        this.at = at;
        getStyleClass().add("enter-injection-form");
        getChildren().addAll(
                drugInput,
                createKindInput(),
                createCommands(),
                createSearchTextInput(),
                createSearchResult()
        );
    }

    private Node createKindInput(){
        HBox hbox = new HBox(4);
        kindGroup = new RadioButtonGroup<>();
        kindGroup.createRadioButton("皮下・筋肉", ConductKind.HikaChuusha);
        kindGroup.createRadioButton("静脈 ", ConductKind.JoumyakuChuusha);
        kindGroup.createRadioButton("その他", ConductKind.OtherChuusha);
        kindGroup.setValue(ConductKind.HikaChuusha);
        hbox.getChildren().addAll(kindGroup.getButtons());
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> onCancel(this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private Node createSearchTextInput(){
        return new SearchTextBox(){
            @Override
            protected void onEnter(String text) {
                Context.getInstance().getFrontend().searchIyakuhinMaster(text, LocalDate.parse(at))
                        .thenAccept(result -> Platform.runLater(() -> {
                            searchResult.setList(result);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }
        };
    }

    private Node createSearchResult(){
        searchResult = new SearchResult<>();
        searchResult.setConverter(m -> m.name);
        searchResult.setOnSelectCallback(drugInput::setMaster);
        return searchResult;
    }

    private void doEnter(){
        ConductDrugDTO drug = new ConductDrugDTO();
        drug.conductDrugId = conductDrugId;
        drug.conductId = conductId;
        drugInput.stuffInto(drug, d -> onEnter(this, kindGroup.getValue(), d), HandlerFX::alert);
    }

    protected void onEnter(EnterInjectionForm form, ConductKind kind, ConductDrugDTO drug){

    }

    protected void onCancel(EnterInjectionForm form){

    }
}

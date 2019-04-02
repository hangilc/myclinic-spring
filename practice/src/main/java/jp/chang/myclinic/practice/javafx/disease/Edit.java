package jp.chang.myclinic.practice.javafx.disease;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.edit.Form;
import jp.chang.myclinic.practice.javafx.disease.search.SearchBox;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Edit extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private Form form;
    private SearchBox searchBox;
    private DiseaseDTO origDisease;

    public Edit(DiseaseFullDTO disease) {
        super(4);
        this.origDisease = disease.disease;
        form = new Form(disease);
        searchBox = new SearchBox(() -> form.getStartDate()){
            @Override
            public void onByoumeiSelect(ByoumeiMasterDTO master) {
                form.setByoumeiMaster(master);
            }

            @Override
            public void onShuushokugoSelect(ShuushokugoMasterDTO master) {
                form.addShuushokugoMaster(master);
            }
        };
        getChildren().addAll(
                form,
                createCommands(),
                searchBox
        );
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Hyperlink deleteAdjLink = new Hyperlink("修飾語削除");
        Hyperlink deleteLink = new Hyperlink("削除");
        enterButton.setOnAction(evt -> doEnter());
        deleteAdjLink.setOnAction(evt -> form.deleteShuushokugoMaster());
        deleteLink.setOnAction(evt -> doDelete());
        hbox.getChildren().addAll(
                enterButton,
                deleteAdjLink,
                deleteLink
        );
        return hbox;
    }

    private void doEnter(){
        DiseaseModifyDTO modify = new DiseaseModifyDTO();
        DiseaseDTO disease = DiseaseDTO.copy(origDisease);
        disease.shoubyoumeicode = form.getShoubyoumeicode();
        Result<LocalDate, List<String>> dateResult = form.getStartDate();
        if( dateResult.hasValue() ){
            disease.startDate = dateResult.getValue().toString();
        } else {
            GuiUtil.alertError("開始日の設定が不適切です。");
            return;
        }
        dateResult = form.getEndDate();
        if( dateResult.hasValue() ){
            LocalDate d = dateResult.getValue();
            if( d == null ){
                disease.endDate = "0000-00-00";
            } else {
                disease.endDate = d.toString();
            }
        } else {
            GuiUtil.alertError("終了日の設定が不適切です。");
            return;
        }
        disease.endReason = form.getEndReason().getCode();
        modify.disease = disease;
        modify.shuushokugocodes = form.getShuushokugoMasters().stream()
                .map(m -> m.shuushokugocode).collect(Collectors.toList());
        Context.getInstance().getFrontend().modifyDisease(modify)
                .thenAccept(result -> onComplete())
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDelete(){
        if( !GuiUtil.confirm("この病名を削除していいですか？") ){
            return;
        }
        int diseaseId = origDisease.diseaseId;
        Context.getInstance().getFrontend().deleteDisease(diseaseId)
                .thenAccept(result -> Platform.runLater(this::onComplete))
                .exceptionally(HandlerFX::exceptionally);
    }

    protected void onComplete(){

    }

}

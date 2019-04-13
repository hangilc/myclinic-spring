package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

public class ShinryouEditForm extends WorkForm {

    private int shinryouId;
    private ShinryouAttrDTO attr;
    private ShinryouInput shinryouInput;
    private HBox commandBox = new HBox(4);

    public ShinryouEditForm(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        super("診療行為編集");
        this.shinryouId = shinryou.shinryou.shinryouId;
        this.attr = ShinryouAttrDTO.copy(attr);
        this.shinryouInput = new ShinryouInput(shinryou.master, attr);
        setupCommandBox(attr);
        getChildren().addAll(
                shinryouInput,
                commandBox
        );
    }

    private void setupCommandBox(ShinryouAttrDTO attr){
        HBox hbox = commandBox;
        hbox.getChildren().clear();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button deleteButton = new Button("削除");
        Button cancelButton = new Button("閉じる");
        deleteButton.setOnAction(event -> onDelete(this));
        cancelButton.setOnAction(event -> onCancel(this));
        hbox.getChildren().addAll(deleteButton, cancelButton);
        if( attr != null ){
            Hyperlink editTekiyou = new Hyperlink("摘要編集");
            Hyperlink deleteTekiyou = new Hyperlink("摘要削除");
            editTekiyou.setOnAction(evt -> doModifyTekiyou(attr.tekiyou));
            deleteTekiyou.setOnAction(evt -> doDeleteTekiyou());
            hbox.getChildren().addAll(editTekiyou, deleteTekiyou);
        } else {
            Hyperlink addTekiyouLink = new Hyperlink("摘要追加");
            addTekiyouLink.setOnAction(evt -> doModifyTekiyou(""));
            hbox.getChildren().add(addTekiyouLink);
        }
    }

    private void doDeleteTekiyou(){
        if( GuiUtil.confirm("この摘要を削除していいですか？") ){
            Frontend frontend = Context.frontend;
            frontend.deleteShinryouTekiyou(shinryouId)
                    .thenCompose(v -> frontend.getShinryouAttr(shinryouId))
                    .thenAccept(attr -> Platform.runLater(() -> {
                        shinryouInput.deleteTekiyou();
                        setupCommandBox(attr);
                        onAttrModified(attr);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doModifyTekiyou(String orig){
        Frontend frontend = Context.frontend;
        GuiUtil.askForString("摘要の内容", orig)
                .ifPresent(tekiyou -> {
                    frontend.setShinryouTekiyou(shinryouId, tekiyou)
                            .thenCompose(v -> frontend.getShinryouAttr(shinryouId))
                            .thenAccept(modified -> Platform.runLater(() -> {
                                shinryouInput.setTekiyou(modified.tekiyou);
                                setupCommandBox(modified);
                                onAttrModified(modified);
                            }))
                            .exceptionally(HandlerFX::exceptionally);
                });
    }

    protected void onAttrModified(ShinryouAttrDTO modified){

    }

    protected void onDelete(ShinryouEditForm form){

    }

    protected void onCancel(ShinryouEditForm form){

    }

}

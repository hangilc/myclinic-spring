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
import jp.chang.myclinic.utilfx.ConfirmDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.Consumer;

public class ShinryouEditForm extends WorkForm {

    private int shinryouId;
    private ShinryouAttrDTO attr;
    private ShinryouInput shinryouInput;
    private HBox commandBox = new HBox(4);
    private HBox tekiyouBox = new HBox(4);
    private Consumer<ShinryouAttrDTO> onEnteredHandler = attr -> {};
    private Runnable onCancelHandler = () -> {};
    private Runnable onDeletedHandler = () -> {};

    public ShinryouEditForm(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        super("診療行為編集");
        this.shinryouId = shinryou.shinryou.shinryouId;
        this.attr = ShinryouAttrDTO.copy(attr);
        this.shinryouInput = new ShinryouInput();
        shinryouInput.setMaster(shinryou.master);
        String tekiyou = ShinryouAttrDTO.extractTekiyou(attr);
        if( tekiyou != null ){
            shinryouInput.setTekiyou(tekiyou);
        }
        setupCommandBox(attr);
        getChildren().addAll(
                shinryouInput,
                commandBox
        );
    }

    public void setOnEnteredHandler(Consumer<ShinryouAttrDTO> handler){
        this.onEnteredHandler = handler;
    }

    public void setOnCancelHandler(Runnable onCancelHandler) {
        this.onCancelHandler = onCancelHandler;
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    private void setupCommandBox(ShinryouAttrDTO attr){
        HBox hbox = commandBox;
        hbox.getChildren().clear();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Button deleteButton = new Button("削除");
        enterButton.setOnAction(event -> onEnter());
        cancelButton.setOnAction(event ->onCancelHandler.run());
        deleteButton.setOnAction(event -> onDelete());
        hbox.getChildren().addAll(enterButton, cancelButton, deleteButton, tekiyouBox);
        tekiyouBox.setAlignment(Pos.CENTER_LEFT);
        adaptTekiyouBox();
    }

    private void adaptTekiyouBox(){
        if( attr != null ){
            Hyperlink editTekiyou = new Hyperlink("摘要編集");
            Hyperlink deleteTekiyou = new Hyperlink("摘要削除");
            editTekiyou.setOnAction(evt -> doModifyTekiyou(attr.tekiyou));
            deleteTekiyou.setOnAction(evt -> doDeleteTekiyou());
            tekiyouBox.getChildren().setAll(editTekiyou, deleteTekiyou);
        } else {
            Hyperlink addTekiyouLink = new Hyperlink("摘要追加");
            addTekiyouLink.setOnAction(evt -> doModifyTekiyou(""));
            tekiyouBox.getChildren().setAll(addTekiyouLink);
        }
    }

    private void doDeleteTekiyou(){
        if( GuiUtil.confirm("この摘要を削除していいですか？") ){
            this.attr = ShinryouAttrDTO.setTekiyou(shinryouId, attr, null);
            shinryouInput.deleteTekiyou();
            adaptTekiyouBox();
        }
    }

    private void doModifyTekiyou(String orig){
        GuiUtil.askForString("摘要の内容", orig)
                .ifPresent(tekiyou -> {
                    this.attr = ShinryouAttrDTO.setTekiyou(shinryouId, attr, tekiyou);
                    shinryouInput.setTekiyou(tekiyou);
                    adaptTekiyouBox();
                });
    }

    private void onEnter(){
        Context.frontend.setShinryouAttr(shinryouId, attr)
                .thenAcceptAsync(v -> {
                    onEnteredHandler.accept(attr);
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void onDelete(){
        if( ConfirmDialog.confirm("この摘要を削除しますか？", ShinryouEditForm.this) ) {
            Context.frontend.deleteShinryou(shinryouId)
                    .thenAccept(v -> onDeletedHandler.run())
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }
}

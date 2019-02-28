package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;

import java.util.Optional;
import java.util.function.Consumer;

public class RecordText extends StackPane {

    private int textId;
    private int visitId;
    private TextRequirement textReq;
    private Runnable onDeletedCallback;
    private Consumer<TextDTO> onCopiedCallback;

    RecordText(TextDTO text) {
        this.textId = text.textId;
        this.visitId = text.visitId;
        TextDisp disp = createDisp(text.content);
        getChildren().add(disp);
    }

    private TextDisp createDisp(String content) {
        TextDisp disp = new TextDisp(content);
        disp.setOnMouseClicked(event -> onDispClicked(disp));
        return disp;
    }

    int getTextId() {
        return textId;
    }

    boolean isDisplaying() {
        return findTextDisp().isPresent();
    }

    void setOnDeletedCallback(Runnable callback){
        this.onDeletedCallback = callback;
    }

    void setOnCopiedCallback(Consumer<TextDTO> callback){
        this.onCopiedCallback = callback;
    }

    private <T> Optional<T> findInChildren(Class<T> childClass){
        for (Node n : getChildren()) {
            if (childClass.isInstance(n)) {
                return Optional.of(childClass.cast(n));
            }
        }
        return Optional.empty();
    }

    public Optional<TextDisp> findTextDisp() {
        return findInChildren(TextDisp.class);
    }

    Optional<TextEditForm> findTextEditForm(){
        return findInChildren(TextEditForm.class);
    }

    void setTextRequirement(TextRequirement textReq) {
        this.textReq = textReq;
    }

    private TextRequirement getTextReq() {
        return textReq;
    }

    private void onDispClicked(TextDisp disp) {
        TextDTO text = new TextDTO();
        text.textId = textId;
        text.visitId = visitId;
        text.content = disp.getContent();
        TextEditForm form = new TextEditForm(text, getTextReq());
        form.setOnUpdated(updatedText -> getChildren().setAll(createDisp(updatedText.content)));
        form.setOnCancel(() -> getChildren().setAll(disp));
        form.setOnDeleted(() -> {
            if( onDeletedCallback != null ){
                onDeletedCallback.run();
            }
        });
        form.setOnCopied(newText -> {
            if( onCopiedCallback != null ){
                onCopiedCallback.accept(newText);
            }
            getChildren().setAll(disp);
        });
        getChildren().setAll(form);
    }

}



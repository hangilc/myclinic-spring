package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextLib;

import java.util.Optional;

public class RecordText extends StackPane {

    private int textId;
    private int visitId;
    private TextLib textLib;

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

    public boolean isDisplaying() {
        return findTextDisp().isPresent();
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

    public Optional<TextEditForm> findTextEditForm(){
        return findInChildren(TextEditForm.class);
    }

    public void setTextLib(TextLib textLib) {
        this.textLib = textLib;
    }

    private TextLib getTextLib() {
        return textLib != null ? textLib : Globals.getInstance().getTextLib();
    }

    private void onDispClicked(TextDisp disp) {
        TextDTO text = new TextDTO();
        text.textId = textId;
        text.visitId = visitId;
        text.content = disp.getContent();
        TextEditForm form = new TextEditForm(text);
        form.setTextLib(getTextLib());
        form.setOnUpdated(updatedText -> getChildren().setAll(createDisp(updatedText.content)));
        form.setOnCancel(() -> getChildren().setAll(disp));
        getChildren().setAll(form);
    }

}



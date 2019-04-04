package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.parts.SearchInputBox;
import jp.chang.myclinic.practice.javafx.parts.SearchResult;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.time.LocalDate;

public class ShinryouForm extends WorkForm {

    private String at;
    private ShinryouInput shinryouInput;
    private SearchResult<ShinryouMasterDTO> searchResult;

    public ShinryouForm(String title, String at){
        super(title);
        getStyleClass().add("shinryou-form");
        this.at = at;
        getChildren().addAll(
                createInput(),
                createCommands(),
                createSearchInput(),
                createSearchResult()
        );
    }

    private Node createInput(){
        shinryouInput = new ShinryouInput();
        return shinryouInput;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onClose(this));
        hbox.getChildren().addAll(enterButton, closeButton);
        return hbox;
    }

    private Node createSearchInput(){
        SearchInputBox box = new SearchInputBox();
        box.setOnTextCallback(text -> {
            if( !text.isEmpty() ) {
                Context.frontend.searchShinryouMaster(text, LocalDate.parse(at))
                        .thenAccept(result -> Platform.runLater(() -> searchResult.setList(result)))
                        .exceptionally(ex -> {
                            FunJavaFX.createErrorHandler().accept(ex);
                            return null;
                        });
            }
        });
        return box;
    }

    private Node createSearchResult(){
        searchResult = new SearchResult<>();
        searchResult.setConverter(m -> m.name);
        searchResult.setOnSelectCallback(shinryouInput::setMaster);
        return searchResult;
    }

    private void doEnter(){
        int shinryoucode = shinryouInput.getShinryoucode();
        if( shinryoucode != 0 ){
            onEnter(shinryoucode);
        }
    }

    protected void onEnter(int shinryoucode){

    }

    protected void onClose(ShinryouForm form){

    }
}

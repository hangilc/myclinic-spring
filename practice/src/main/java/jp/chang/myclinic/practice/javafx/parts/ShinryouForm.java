package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.javafx.FunJavaFX;

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
        SearchInputBox box = new SearchInputBox(){
            @Override
            protected void onSearch(String text) {
                if( !text.isEmpty() ) {
                    FunJavaFX.INSTANCE.searchShinryouMaster(text, at, list -> {
                        searchResult.setList(list);
                    });
                }
            }
        };
        return box;
    }

    private Node createSearchResult(){
        searchResult = new SearchResult<ShinryouMasterDTO>(m -> m.name){
            @Override
            protected void onSelect(ShinryouMasterDTO selected) {
                shinryouInput.setMaster(selected);
            }
        };
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

package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SearchTextInput;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;

import java.util.function.Consumer;

public class DiseaseSearchTextInput extends VBox implements SearchTextInput {

    private Consumer<String> onSearchCallback = s -> {};
    private TextField textField = new TextField();
    private RadioButtonGroup<DiseaseSearcher> modeGroup = new RadioButtonGroup<>();

    public DiseaseSearchTextInput(){
        super(4);
        getChildren().addAll(
                createInput(),
                createSwitch()
        );
    }

    public DiseaseSearcher getSearcher(){
        return modeGroup.getValue();
    }

    private Node createInput(){
        HBox hbox = new HBox(4);
        Button searchButton = new Button("検索");
        Hyperlink exampleLink = new Hyperlink("例");
        textField.setOnAction(evt -> doSearch());
        searchButton.setOnAction(evt -> doSearch());
        hbox.getChildren().addAll(
                textField,
                searchButton,
                exampleLink
        );
        return hbox;
    }

    private Node createSwitch(){
        HBox hbox = new HBox(4);
        modeGroup.createRadioButton("傷病名", DiseaseSearchers.byoumeiSearcher);
        modeGroup.createRadioButton("修飾語", DiseaseSearchers.shuushokugoSearcher);
        modeGroup.setValue(DiseaseSearchers.byoumeiSearcher);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

    private void doSearch(){
        String text = textField.getText();
        if( !text.isEmpty() ){
            onSearchCallback.accept(text);
        }
    }

    @Override
    public void setOnSearchCallback(Consumer<String> cb) {
        this.onSearchCallback = cb;
    }
}

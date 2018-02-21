package jp.chang.myclinic.practice.javafx.disease.add;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.javafx.parts.searchbox.ExtendedSearchTextInput;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiseaseSearchTextInput extends ExtendedSearchTextInput {

    private static Logger logger = LoggerFactory.getLogger(DiseaseSearchTextInput.class);

    private RadioButtonGroup<DiseaseSearcher> modeGroup = new RadioButtonGroup<>();

    public DiseaseSearchTextInput() {
        Hyperlink exampleLink = new Hyperlink("例");
        extendBasic(exampleLink);
        addRow(createSwitch());
    }

    public CompletableFuture<List<DiseaseSearchResultModel>> search(String text, String at){
        return modeGroup.getValue().search(text, at);
    }

    private Node createSwitch(){
        HBox hbox = new HBox(4);
        modeGroup.createRadioButton("病名", DiseaseSearchers.byoumeiSearcher);
        modeGroup.createRadioButton("修飾語", DiseaseSearchers.shuushokugoSearcher);
        modeGroup.setValue(DiseaseSearchers.byoumeiSearcher);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

}

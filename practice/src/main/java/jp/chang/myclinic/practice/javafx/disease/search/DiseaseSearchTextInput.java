package jp.chang.myclinic.practice.javafx.disease.search;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.javafx.parts.searchbox.ExtendedSearchTextInput;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiseaseSearchTextInput extends ExtendedSearchTextInput {

    private static Logger logger = LoggerFactory.getLogger(DiseaseSearchTextInput.class);

    private RadioButtonGroup<DiseaseSearcher> modeGroup = new RadioButtonGroup<>();
    private Runnable onExampleCallback = () -> {};

    public DiseaseSearchTextInput() {
        Hyperlink exampleLink = new Hyperlink("例");
        exampleLink.setOnAction(evt -> onExampleCallback.run());
        extendBasic(exampleLink);
        addRow(createSwitch());
    }

    public DiseaseSearcher getSearcher(){
        return modeGroup.getValue();
    }

    public void setOnExampleCallback(Runnable onExampleCallback) {
        this.onExampleCallback = onExampleCallback;
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

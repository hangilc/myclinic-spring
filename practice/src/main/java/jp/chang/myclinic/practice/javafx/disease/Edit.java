package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.parts.SelectableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edit extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private SelectableList<>
    public Edit(DiseaseFullDTO disease) {
        super(4);
        getChildren().addAll(
                createSearchResult()
        );
    }

    private Node createSearchResult(){

    }

}

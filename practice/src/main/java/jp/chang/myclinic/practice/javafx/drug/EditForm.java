package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private Input input = new Input();

    public EditForm(DrugFullDTO drug, DrugAttrDTO attr) {
        super(4);
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        getChildren().addAll(createTitle(), input);
        input.setDrug(drug, attr);
    }

    private Node createTitle() {
        Label title = new Label("処方の編集");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }


}

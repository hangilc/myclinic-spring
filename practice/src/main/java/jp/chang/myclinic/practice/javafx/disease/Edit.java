package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.disease.edit.Form;
import jp.chang.myclinic.practice.javafx.disease.search.SearchBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edit extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private Form form;
    private SearchBox searchBox;
    private int diseaseId;

    public Edit(DiseaseFullDTO disease) {
        super(4);
        this.diseaseId = disease.disease.diseaseId;
        form = new Form(disease);
        searchBox = new SearchBox(() -> form.getStartDate());
        getChildren().addAll(
                form,
                searchBox
        );
    }

}

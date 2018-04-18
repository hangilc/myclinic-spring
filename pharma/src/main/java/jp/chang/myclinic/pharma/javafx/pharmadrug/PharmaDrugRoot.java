package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class PharmaDrugRoot extends HBox {

    private static Logger logger = LoggerFactory.getLogger(PharmaDrugRoot.class);

    PharmaDrugRoot(List<PharmaDrugNameDTO> pharmaDrugNames) {
        super(4);
        PharmaDrugList pharmaDrugList = new PharmaDrugList(pharmaDrugNames);
        getChildren().addAll(pharmaDrugList);
    }

}

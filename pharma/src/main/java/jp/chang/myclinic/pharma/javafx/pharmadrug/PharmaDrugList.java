package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PharmaDrugList extends ListView<PharmaDrugNameDTO> {

    private static Logger logger = LoggerFactory.getLogger(PharmaDrugList.class);

    PharmaDrugList() {
        setCellFactory(listView -> new ListCell<PharmaDrugNameDTO>(){
            @Override
            protected void updateItem(PharmaDrugNameDTO item, boolean empty) {
                super.updateItem(item, empty);
                if( empty || item == null ){
                    setText("");
                } else {
                    setText(item.name);
                }
            }
        });
    }

}

package jp.chang.myclinic.pharma.javafx.records;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitTextDrugDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Records extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Records.class);

    public Records() {
        super(4);
    }

    public void setItems(List<VisitTextDrugDTO> items, String hilight){
        getChildren().clear();
        items.forEach(item -> {
            Title title = new Title(item.visit.visitedAt);
            Body body = new Body(item);
            getChildren().addAll(
                    title,
                    body
            );
        });
    }

}

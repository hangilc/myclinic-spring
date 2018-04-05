package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextVisitPatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

class Result extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Result.class);

    Result() {
        super(4);
    }

    void set(List<TextVisitPatientDTO> list){
        List<ResultItem> items = list.stream().map(ResultItem::new).collect(Collectors.toList());
        getChildren().setAll(items);
    }

}

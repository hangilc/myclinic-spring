package jp.chang.myclinic.pharma.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class RightColumn extends VBox {

    private static Logger logger = LoggerFactory.getLogger(RightColumn.class);
    private PrescPane prescPane = new PrescPane();
    private AuxSwitch auxSwitch;
    private AuxNav auxNav;
    private AuxRecords auxRecords;

    RightColumn() {
        super(4);
        setVisible(false);
        getStyleClass().add("right-column");
        getChildren().addAll(
                prescPane,
                createAuxSwitch(),
                createAuxNav(),
                createAuxRecords()
        );
    }

    void startPresc(PharmaQueueFullDTO item, List<DrugFullDTO> drugs){
        if( item != null ){
            prescPane.setItem(item, drugs);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private Node createAuxSwitch(){
        auxSwitch = new AuxSwitch();
        return auxSwitch;
    }

    private Node createAuxNav(){
        auxNav = new AuxNav();
        return auxNav;
    }

    private Node createAuxRecords(){
        auxRecords = new AuxRecords();
        return auxRecords;
    }
}

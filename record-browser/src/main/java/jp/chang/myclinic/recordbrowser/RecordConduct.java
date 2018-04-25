package jp.chang.myclinic.recordbrowser;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;

class RecordConduct extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordConduct.class);

    RecordConduct(ConductFullDTO conduct) {
        super(0);
        getChildren().addAll(
                new RecordConductKind(conduct.conduct.kind),
                new RecordConductLabel(conduct.gazouLabel)
        );
        conduct.conductShinryouList.forEach(this::addShinryou);
        conduct.conductDrugs.forEach(this::addDrug);
        conduct.conductKizaiList.forEach(this::addKizai);
    }

    private void addShinryou(ConductShinryouFullDTO shinryou){
        getChildren().add(new RecordConductShinryou(shinryou));
    }

    private void addDrug(ConductDrugFullDTO drug){
        getChildren().add(new RecordConductDrug(drug));
    }

    private void addKizai(ConductKizaiFullDTO kizai){
        getChildren().add(new RecordConductKizai(kizai));
    }

}

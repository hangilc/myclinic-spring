package jp.chang.myclinic.recordbrowser;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.VisitChargePatientDTO;

import java.util.List;

class ListChargeDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ListChargeDialog.class);

    ListChargeDialog(List<VisitChargePatientDTO> list) {
        setTitle("請求金額一覧");
        Pane root = createRoot();
    }

    private Pane createRoot(){
        
    }

}

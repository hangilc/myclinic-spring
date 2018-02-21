package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.disease.end.DateControl;
import jp.chang.myclinic.practice.javafx.disease.end.DiseaseList;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class End extends VBox {

    private static Logger logger = LoggerFactory.getLogger(End.class);
    private DiseaseList diseaseList;
    private DateInput dateInput;
    private RadioButtonGroup<DiseaseEndReason> reasonGroup = new RadioButtonGroup<>();

    public End(List<DiseaseFullDTO> diseases) {
        super(4);
        diseaseList = new DiseaseList(diseases);
        dateInput = new DateInput();
        dateInput.setGengou(Gengou.Current);
        DateControl dateControl = new DateControl();
        Button enterButton = new Button("入力");
        getChildren().addAll(
                diseaseList,
                dateInput,
                dateControl,
                createReasonGroup(),
                enterButton
        );
    }

    private Node createReasonGroup(){
        HBox hbox = new HBox(4);
        reasonGroup.createRadioButton("治癒", DiseaseEndReason.Cured);
        reasonGroup.createRadioButton("中止", DiseaseEndReason.Stopped);
        reasonGroup.createRadioButton("死亡", DiseaseEndReason.Dead);
        reasonGroup.setValue(DiseaseEndReason.Cured);
        hbox.getChildren().addAll(reasonGroup.getButtons());
        return hbox;
    }

}

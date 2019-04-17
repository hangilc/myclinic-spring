package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.ConductShinryouValidator;

public class ShinryouInput extends DispGrid {

    private int shinryoucode = 0;
    private Text name = new Text("");

    ShinryouInput(){
        addRow("名称：", new TextFlow(name));
    }

    private int getShinryoucode() {
        return shinryoucode;
    }

    private void setShinryoucode(int shinryoucode) {
        this.shinryoucode = shinryoucode;
    }

    private void setName(String name) {
        this.name.setText(name);
    }

    void setMaster(ShinryouMasterDTO master){
        setShinryoucode(master.shinryoucode);
        setName(master.name);
    }

    Validated<ConductShinryouDTO> getValidatedToEnter(int conductId){
        return new ConductShinryouValidator()
                .setValidatedConductShinryouId(0)
                .validateConductId(conductId)
                .validateShinryoucode(getShinryoucode())
                .validate();
    }
}

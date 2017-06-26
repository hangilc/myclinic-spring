package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.util.List;

public class AuxDispDrugs extends JPanel {
    private PatientDTO patient;
    private List<IyakuhincodeNameDTO> iyakuhinList;

    public AuxDispDrugs(PatientDTO patient, List<IyakuhincodeNameDTO> iyakuhinList){
        this.patient = patient;
        this.iyakuhinList = iyakuhinList;

    }
}

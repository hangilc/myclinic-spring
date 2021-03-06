package jp.chang.myclinic.pharma.drawercreator;

import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescContentDataCreator {

    private String patientName;
    private String prescDate;
    private List<String> drugs = new ArrayList<>();

    public PrescContentDataCreator(PatientDTO patient, LocalDate at, List<DrugFullDTO> drugs){
        patientName = patient.lastName + " " + patient.firstName;
        prescDate = new KanjiDateRepBuilder(at).format1().build();
        int index = 1;
        for(DrugFullDTO drug: drugs){
            this.drugs.add(index + ") " + DrugUtil.drugRep(drug));
            index += 1;
        }
    }

    public PrescContentDrawerData createData(){
        PrescContentDrawerData data = new PrescContentDrawerData();
        data.patientName = patientName;
        data.prescDate = prescDate;
        data.drugs = drugs;
        return data;
    }
}

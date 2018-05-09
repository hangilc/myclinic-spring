package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DiseaseDTO;

import java.time.LocalDate;

public class DiseaseModifier {

    private DiseaseDTO disease;

    DiseaseModifier(DiseaseDTO disease) {
        this.disease = disease;
    }

    public DiseaseModifier setStartDate(LocalDate startDate){
        disease.startDate = startDate.toString();
        return this;
    }

}

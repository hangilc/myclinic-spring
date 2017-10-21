package jp.chang.myclinic.practice.rightpane.searchpatient;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.lib.SearchResultList;

class SearchResult extends SearchResultList<PatientDTO> {

    @Override
    protected String dataToRep(PatientDTO patient) {
        return String.format("[%04d] %s %s (%s %s)", patient.patientId, patient.lastName,
                patient.firstName, patient.lastNameYomi, patient.firstNameYomi);
    }

}

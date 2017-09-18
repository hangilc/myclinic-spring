package jp.chang.myclinic.practice.rightpane.todaysvisits;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.lib.SearchResultList;

class SearchResult extends SearchResultList<VisitPatientDTO> {

    interface Callback {
        default void onSelect(VisitPatientDTO selected){}
    }

    private Callback callback = new Callback(){};

    SearchResult(){
        setDoubleClickHandler(data -> callback.onSelect(data));
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    protected String dataToRep(VisitPatientDTO data){
        PatientDTO patient = data.patient;
        return String.format("[%d] %s %s", patient.patientId, patient.lastName, patient.firstName);
    }
}

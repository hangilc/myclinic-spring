package jp.chang.myclinic.reception;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;

class WqueueData {

	private final WqueueWaitState state;
	private final String label;
	private final int visitId;
	private final PatientDTO patient;

	public WqueueData(WqueueWaitState state, String label, int visitId, PatientDTO patient){
		this.state = state;
		this.label = label;
		this.visitId = visitId;
		this.patient = patient;
	}

	public WqueueWaitState getState(){
		return state;
	}

	public String getLabel(){
		return label;
	}

	public int getVisitId() {
		return visitId;
	}

	public PatientDTO getPatient() {
		return patient;
	}

}
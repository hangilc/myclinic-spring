package jp.chang.myclinic;

import jp.chang.myclinic.dto.*;

class Broadcast {

	public static BroadcastChannel<PatientDTO> patientModified = new BroadcastChannel<>();
}
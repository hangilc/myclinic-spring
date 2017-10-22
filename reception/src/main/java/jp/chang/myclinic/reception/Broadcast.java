package jp.chang.myclinic.reception;

import jp.chang.myclinic.dto.*;

class Broadcast {

	public static BroadcastChannel<PatientDTO> patientModified = new BroadcastChannel<>();
}
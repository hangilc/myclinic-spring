package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.*;
import java.sql.Date;

public class JsonMapper {

	public static PatientJS toPatientJS(Patient patient){
		PatientJS patientJS = new PatientJS();
		patientJS.patientId = patient.getPatientId();
		patientJS.lastName = patient.getLastName();
		patientJS.firstName = patient.getFirstName();
		patientJS.lastNameYomi = patient.getLastNameYomi();
		patientJS.firstNameYomi = patient.getFirstNameYomi();
		patientJS.birthday = nullableDateToString(patient.getBirthday());
		patientJS.sex = patient.getSex();
		patientJS.address = patient.getAddress();
		patientJS.phone = patient.getPhone();
		return patientJS;
	}

	public static WqueueJS toWqueueJS(Wqueue wqueue){
		WqueueJS js = new WqueueJS();
		js.visitId = wqueue.getVistiId();
		js.waitState = wqueue.getWaitState().ordinal();
		return js;
	}

	private static String nullableDateToString(Date date){
		if( date == null ){
			return null;
		} else {
			return date.toString();
		}
	}
}
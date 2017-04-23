package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.*;
import java.sql.Date;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

	public PatientDTO toPatientDTO(Patient patient){
		PatientDTO patientDTO = new PatientDTO();
		patientDTO.patientId = patient.getPatientId();
		patientDTO.lastName = patient.getLastName();
		patientDTO.firstName = patient.getFirstName();
		patientDTO.lastNameYomi = patient.getLastNameYomi();
		patientDTO.firstNameYomi = patient.getFirstNameYomi();
		patientDTO.birthday = nullableDateToString(patient.getBirthday());
		patientDTO.sex = patient.getSex();
		patientDTO.address = patient.getAddress();
		patientDTO.phone = patient.getPhone();
		return patientDTO;
	}

	public WqueueDTO toWqueueDTO(Wqueue wqueue){
		WqueueDTO wqueueDTO = new WqueueDTO();
		wqueueDTO.visitId = wqueue.getVisitId();
		wqueueDTO.waitState = wqueue.getWaitState();
		return wqueueDTO;
	}

	private String nullableDateToString(Date date){
		if( date == null ){
			return null;
		} else {
			return date.toString();
		}
	}
}
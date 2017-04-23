package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");

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

	public Patient fromPatientDTO(PatientDTO patientDTO){
		Patient patient = new Patient();
		patient.setPatientId(patientDTO.patientId);
		patient.setLastName(patientDTO.lastName);
		patient.setFirstName(patientDTO.firstName);
		patient.setLastNameYomi(patientDTO.lastNameYomi);
		patient.setFirstNameYomi(patientDTO.firstNameYomi);
		patient.setBirthday(stringToDate(patientDTO.birthday));
		patient.setSex(patientDTO.sex);
		patient.setAddress(patientDTO.address);
		patient.setPhone(patientDTO.phone);
		return patient;
	}

	public WqueueDTO toWqueueDTO(Wqueue wqueue){
		WqueueDTO wqueueDTO = new WqueueDTO();
		wqueueDTO.visitId = wqueue.getVisitId();
		wqueueDTO.waitState = wqueue.getWaitState();
		return wqueueDTO;
	}

	public VisitDTO toVisitDTO(Visit visit){
		VisitDTO visitDTO = new VisitDTO();
		visitDTO.visitId = visit.getVisitId();
		visitDTO.patientId = visit.getPatientId();
		visitDTO.visitedAt = timestampToString(visit.getVisitedAt());
		visitDTO.shahokokuhoId = visit.getShahokokuhoId();
		visitDTO.koukikoureiId = visit.getKoukikoureiId();
		visitDTO.roujinId = visit.getRoujinId();
		visitDTO.kouhi1Id = visit.getKouhi1Id();
		visitDTO.kouhi2Id = visit.getKouhi2Id();
		visitDTO.kouhi3Id = visit.getKouhi3Id();
		return visitDTO;
	}

	private String nullableDateToString(Date date){
		if( date == null ){
			return null;
		} else {
			return date.toString();
		}
	}

	private Date stringToDate(String str){
		if( str == null ){
			return null;
		} else if( "0000-00-00".equals(str) ){
			return null;
		} else {
			LocalDate d = LocalDate.parse(str, dateFormatter);
			return Date.valueOf(d);
		}
	}

	private String timestampToString(Timestamp ts){
		LocalDateTime dt = ts.toLocalDateTime();
		return dt.format(dateTimeFormatter);
	}
}
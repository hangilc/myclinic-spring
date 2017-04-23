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

	public ShahokokuhoDTO toShahokokuhoDTO(Shahokokuho shahokokuho){
		ShahokokuhoDTO shahokokuhoDTO = new ShahokokuhoDTO();
		shahokokuhoDTO.shahokokuhoId = shahokokuho.getShahokokuhoId();
		shahokokuhoDTO.patientId = shahokokuho.getPatientId();
		shahokokuhoDTO.hokenshaBangou = shahokokuho.getHokenshaBangou();
		shahokokuhoDTO.hihokenshaBangou = shahokokuho.getHihokenshaBangou();
		shahokokuhoDTO.hihokenshaKigou= shahokokuho.getHihokenshaKigou();
		shahokokuhoDTO.honnin = shahokokuho.getHonnin();
		shahokokuhoDTO.kourei = shahokokuho.getKourei();
		shahokokuhoDTO.validFrom = shahokokuho.getValidFrom().toString();
		shahokokuhoDTO.validUpto = shahokokuho.getValidUpto();
		return shahokokuhoDTO;
	}

	public Shahokokuho fromShahokokuhoDTO(ShahokokuhoDTO shahokokuhoDTO){
		Shahokokuho shahokokuho = new Shahokokuho();
		shahokokuho.setShahokokuhoId(shahokokuhoDTO.shahokokuhoId);
		shahokokuho.setPatientId(shahokokuhoDTO.patientId);
		shahokokuho.setHokenshaBangou(shahokokuhoDTO.hokenshaBangou);
		shahokokuho.setHihokenshaBangou(shahokokuhoDTO.hihokenshaBangou);
		shahokokuho.setHihokenshaKigou(shahokokuhoDTO.hihokenshaKigou);
		shahokokuho.setHonnin(shahokokuhoDTO.honnin);
		shahokokuho.setKourei(shahokokuhoDTO.kourei);
		shahokokuho.setValidFrom(stringToDate(shahokokuhoDTO.validFrom));
		shahokokuho.setValidUpto(shahokokuhoDTO.validUpto);
		return shahokokuho;
	}

	public KoukikoureiDTO toKoukikoureiDTO(Koukikourei koukikourei){
		KoukikoureiDTO koukikoureiDTO = new KoukikoureiDTO();
		koukikoureiDTO.koukikoureiId = koukikourei.getKoukikoureiId();
		koukikoureiDTO.patientId = koukikourei.getPatientId();
		koukikoureiDTO.hokenshaBangou = koukikourei.getHokenshaBangou();
		koukikoureiDTO.hihokenshaBangou = koukikourei.getHihokenshaBangou();
		koukikoureiDTO.futanWari = koukikourei.getFutanWari();
		koukikoureiDTO.validFrom = koukikourei.getValidFrom().toString();
		koukikoureiDTO.validUpto = koukikourei.getValidUpto();
		return koukikoureiDTO;
	}

	public Koukikourei fromKoukikoureiDTO(KoukikoureiDTO koukikoureiDTO){
		Koukikourei koukikourei = new Koukikourei();
		koukikourei.setKoukikoureiId(koukikoureiDTO.koukikoureiId);
		koukikourei.setPatientId(koukikoureiDTO.patientId);
		koukikourei.setHokenshaBangou(koukikoureiDTO.hokenshaBangou);
		koukikourei.setHihokenshaBangou(koukikoureiDTO.hihokenshaBangou);
		koukikourei.setFutanWari(koukikoureiDTO.futanWari);
		koukikourei.setValidFrom(stringToDate(koukikoureiDTO.validFrom));
		koukikourei.setValidUpto(koukikoureiDTO.validUpto);
		return koukikourei;
	}

	public RoujinDTO toRoujinDTO(Roujin roujin){
		RoujinDTO roujinDTO = new RoujinDTO();
		roujinDTO.roujinId = roujin.getRoujinId();
		roujinDTO.patientId = roujin.getPatientId();
		roujinDTO.shichouson = roujin.getShichouson();
		roujinDTO.jukyuusha = roujin.getJukyuusha();
		roujinDTO.futanWari = roujin.getFutanWari();
		roujinDTO.validFrom = roujin.getValidFrom().toString();
		roujinDTO.validUpto = roujin.getValidUpto();
		return roujinDTO;
	}

	public Roujin fromRoujinDTO(RoujinDTO roujinDTO){
		Roujin roujin = new Roujin();
		roujin.setRoujinId(roujinDTO.roujinId);
		roujin.setPatientId(roujinDTO.patientId);
		roujin.setShichouson(roujinDTO.shichouson);
		roujin.setJukyuusha(roujinDTO.jukyuusha);
		roujin.setFutanWari(roujinDTO.futanWari);
		roujin.setValidFrom(stringToDate(roujinDTO.validFrom));
		roujin.setValidUpto(roujinDTO.validUpto);
		return roujin;
	}

	public KouhiDTO toKouhiDTO(Kouhi kouhi){
		KouhiDTO kouhiDTO = new KouhiDTO();
		kouhiDTO.kouhiId = kouhi.getKouhiId();
		kouhiDTO.patientId = kouhi.getPatientId();
		kouhiDTO.futansha = kouhi.getFutansha();
		kouhiDTO.jukyuusha = kouhi.getJukyuusha();
		kouhiDTO.validFrom = kouhi.getValidFrom().toString();
		kouhiDTO.validUpto = kouhi.getValidUpto();
		return kouhiDTO;
	}

	public Kouhi fromKouhiDTO(KouhiDTO kouhiDTO){
		Kouhi kouhi = new Kouhi();
		kouhi.setKouhiId(kouhiDTO.kouhiId);
		kouhi.setPatientId(kouhiDTO.patientId);
		kouhi.setFutansha(kouhiDTO.futansha);
		kouhi.setJukyuusha(kouhiDTO.jukyuusha);
		kouhi.setValidFrom(stringToDate(kouhiDTO.validFrom));
		kouhi.setValidUpto(kouhiDTO.validUpto);
		return kouhi;
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
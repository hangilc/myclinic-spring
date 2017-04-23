package jp.chang.myclinic.db;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;

import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Date;

import java.util.stream.Stream;
import java.util.stream.Collectors;

@Component
public class DbGateway {
	@Autowired
	private DTOMapper mapper;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private WqueueRepository wqueueRepository;
	@Autowired
	private ShahokokuhoRepository shahokokuhoRepository;
	@Autowired
	private KoukikoureiRepository koukikoureiRepository;
	@Autowired
	private RoujinRepository roujinRepository;
	@Autowired
	private KouhiRepository kouhiRepository;
	@Autowired
	private ChargeRepository chargeRepository;
	@Autowired
	private PaymentRepository paymentRepository;

	public PatientDTO getPatient(int patientId){
		Patient patient = patientRepository.findOne(patientId);
		return mapper.toPatientDTO(patient);
	}

	public List<WqueueFullDTO> listWqueueFull(){
		try(Stream<Wqueue> stream = wqueueRepository.findAllAsStream()){
			return stream.map(wqueue -> {
				WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
				wqueueFullDTO.wqueue = mapper.toWqueueDTO(wqueue);
				wqueueFullDTO.visit = mapper.toVisitDTO(wqueue.getVisit());
				wqueueFullDTO.patient = mapper.toPatientDTO(wqueue.getVisit().getPatient());
				return wqueueFullDTO;
			})
			.collect(Collectors.toList());
		}
	}

	public int enterPatient(PatientDTO patientDTO){
		Patient patient = mapper.fromPatientDTO(patientDTO);
		patient.setPatientId(0);
		patient = patientRepository.save(patient);
		return patient.getPatientId();
	}

	public List<PatientDTO> searchPatientByLastName(String text){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.findByLastNameContaining(text, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public List<PatientDTO> searchPatientByFirstName(String text){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.findByFirstNameContaining(text, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public List<PatientDTO> searchPatientByLastNameYomi(String text){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.findByLastNameYomiContaining(text, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public List<PatientDTO> searchPatientByFirstNameYomi(String text){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.findByFirstNameYomiContaining(text, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO){
		Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
		shahokokuho.setShahokokuhoId(0);
		shahokokuho = shahokokuhoRepository.save(shahokokuho);
		return shahokokuho.getShahokokuhoId();
	}

	public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "shahokokuhoId");
		Date atDate = Date.valueOf(at);
		try(Stream<Shahokokuho> stream = shahokokuhoRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
		}
	}

	public int enterKoukikourei(KoukikoureiDTO koukikoureiDTO){
		Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
		koukikourei.setKoukikoureiId(0);
		koukikourei = koukikoureiRepository.save(koukikourei);
		return koukikourei.getKoukikoureiId();
	}
	
	public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "koukikoureiId");
		Date atDate = Date.valueOf(at);
		try(Stream<Koukikourei> stream = koukikoureiRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
		}
	}

	public int enterRoujin(RoujinDTO roujinDTO){
		Roujin roujin = mapper.fromRoujinDTO(roujinDTO);
		roujin.setRoujinId(0);
		roujin = roujinRepository.save(roujin);
		return roujin.getRoujinId();
	}
	
	public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "roujinId");
		Date atDate = Date.valueOf(at);
		try(Stream<Roujin> stream = roujinRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toRoujinDTO).collect(Collectors.toList());
		}
	}

	public int enterKouhi(KouhiDTO kouhiDTO){
		Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
		kouhi.setKouhiId(0);
		kouhi = kouhiRepository.save(kouhi);
		return kouhi.getKouhiId();
	}
	
	public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.ASC, "kouhiId");
		Date atDate = Date.valueOf(at);
		try(Stream<Kouhi> stream = kouhiRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toKouhiDTO).collect(Collectors.toList());
		}
	}

	public void enterCharge(ChargeDTO chargeDTO){
		Charge charge = mapper.fromChargeDTO(chargeDTO);
		charge = chargeRepository.save(charge);
	}

	public void enterPayment(PaymentDTO paymentDTO){
		Payment payment = mapper.fromPaymentDTO(paymentDTO);
		payment = paymentRepository.save(payment);
	}

}
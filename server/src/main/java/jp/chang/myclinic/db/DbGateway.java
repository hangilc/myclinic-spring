package jp.chang.myclinic.db;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Date;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Optional;

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
	@Autowired
	private VisitRepository visitRepository;
	@Autowired
	private ShinryouRepository shinryouRepository;
	@Autowired
	private DrugRepository drugRepository;
	@Autowired
	private ConductRepository conductRepository;
	@Autowired
	private GazouLabelRepository gazouLabelRepository;
	@Autowired
	private ConductShinryouRepository conductShinryouRepository;
	@Autowired
	private ConductDrugRepository conductDrugRepository;
	@Autowired
	private ConductKizaiRepository conductKizaiRepository;

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

	public void enterWqueue(WqueueDTO wqueueDTO){
		Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
		wqueueRepository.save(wqueue);
	}

	public PatientDTO getPatient(int patientId){
		Patient patient = patientRepository.findOne(patientId);
		return mapper.toPatientDTO(patient);
	}

	public int enterPatient(PatientDTO patientDTO){
		Patient patient = mapper.fromPatientDTO(patientDTO);
		patient = patientRepository.save(patient);
		return patient.getPatientId();
	}

	public void updatePatient(PatientDTO patientDTO){
		Patient patient = mapper.fromPatientDTO(patientDTO);
		patientRepository.save(patient);
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

	public List<PatientDTO> searchPatientByName(String lastName, String firstName){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.searchPatientByName(lastName, firstName, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public List<PatientDTO> searchPatientByYomi(String lastNameYomi, String firstNameYomi){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		try(Stream<Patient> stream = patientRepository.searchPatientByYomi(lastNameYomi, firstNameYomi, sort)){
			return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
		}
	}

	public List<PatientDTO> listRecentlyRegisteredPatients(int n){
		PageRequest pageRequest = new PageRequest(0, n, Sort.Direction.DESC, "patientId");
		return patientRepository.findAll(pageRequest).map(mapper::toPatientDTO).getContent();
	}

	public int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO){
		Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
		shahokokuho = shahokokuhoRepository.save(shahokokuho);
		return shahokokuho.getShahokokuhoId();
	}

	public void deleteShahokokuho(int shahokokuhoId){
		shahokokuhoRepository.delete(shahokokuhoId);
	}

	public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "shahokokuhoId");
		Date atDate = Date.valueOf(at);
		try(Stream<Shahokokuho> stream = shahokokuhoRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
		}
	}

	public List<ShahokokuhoDTO> findShahokokuhoByPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "shahokokuhoId");
		return shahokokuhoRepository.findByPatientId(patientId, sort).stream()
			.map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
	}

	public int enterKoukikourei(KoukikoureiDTO koukikoureiDTO){
		Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
		koukikourei = koukikoureiRepository.save(koukikourei);
		return koukikourei.getKoukikoureiId();
	}

	public void deleteKoukikourei(int koukikoureiId){
		koukikoureiRepository.delete(koukikoureiId);
	}

	public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "koukikoureiId");
		Date atDate = Date.valueOf(at);
		try(Stream<Koukikourei> stream = koukikoureiRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
		}
	}

	public List<KoukikoureiDTO> findKoukikoureiByPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "koukikoureiId");
		return koukikoureiRepository.findByPatientId(patientId, sort).stream()
			.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
	}

	public int enterRoujin(RoujinDTO roujinDTO){
		Roujin roujin = mapper.fromRoujinDTO(roujinDTO);
		roujin = roujinRepository.save(roujin);
		return roujin.getRoujinId();
	}
	
	public void deleteRoujin(int roujinId){
		roujinRepository.delete(roujinId);
	}

	public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.DESC, "roujinId");
		Date atDate = Date.valueOf(at);
		try(Stream<Roujin> stream = roujinRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toRoujinDTO).collect(Collectors.toList());
		}
	}

	public List<RoujinDTO> findRoujinByPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "roujinId");
		return roujinRepository.findByPatientId(patientId, sort).stream()
			.map(mapper::toRoujinDTO).collect(Collectors.toList());
	}

	public int enterKouhi(KouhiDTO kouhiDTO){
		Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
		kouhi = kouhiRepository.save(kouhi);
		return kouhi.getKouhiId();
	}
	
	public void deleteKouhi(int kouhiId){
		kouhiRepository.delete(kouhiId);
	}

	public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at){
		Sort sort = new Sort(Sort.Direction.ASC, "kouhiId");
		Date atDate = Date.valueOf(at);
		try(Stream<Kouhi> stream = kouhiRepository.findAvailable(patientId, atDate, sort)){
			return stream.map(mapper::toKouhiDTO).collect(Collectors.toList());
		}
	}

	public List<KouhiDTO> findKouhiByPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "kouhiId");
		return kouhiRepository.findByPatientId(patientId, sort).stream()
			.map(mapper::toKouhiDTO).collect(Collectors.toList());
	}

	public HokenListDTO findHokenByPatient(int patientId){
		HokenListDTO hokenListDTO = new HokenListDTO();
		hokenListDTO.shahokokuhoListDTO = findShahokokuhoByPatient(patientId);
		hokenListDTO.koukikoureiListDTO = findKoukikoureiByPatient(patientId);
		hokenListDTO.roujinListDTO = findRoujinByPatient(patientId);
		hokenListDTO.kouhiListDTO = findKouhiByPatient(patientId);
		return hokenListDTO;
	}

	public void enterCharge(ChargeDTO chargeDTO){
		Charge charge = mapper.fromChargeDTO(chargeDTO);
		charge = chargeRepository.save(charge);
	}

	public void enterPayment(PaymentDTO paymentDTO){
		Payment payment = mapper.fromPaymentDTO(paymentDTO);
		payment = paymentRepository.save(payment);
	}

	public VisitDTO getVisit(int visitId){
		Visit visit = visitRepository.findOne(visitId);
		return mapper.toVisitDTO(visit);
	}

	public int enterVisit(VisitDTO visitDTO){
		Visit visit = mapper.fromVisitDTO(visitDTO);
		visit = visitRepository.save(visit);
		return visit.getVisitId();
	}

	public ShinryouFullDTO getShinryouFull(int shinryouId){
		Object[] result = shinryouRepository.findOneWithMaster(shinryouId).get(0);
		return resultToShinryouFullDTO(result);
	}

	public List<ShinryouFullDTO> listShinryouFull(int visitId){
		Sort sort = new Sort(Sort.Direction.ASC, "shinryoucode");
		return shinryouRepository.findByVisitIdWithMaster(visitId, sort).stream()
		.map(this::resultToShinryouFullDTO)
		.collect(Collectors.toList());
	}

	public DrugFullDTO getDrugFull(int drugId){
		Object[] result = drugRepository.findOneWithMaster(drugId).get(0);
		return resultToDrugFullDTO(result);
	}

	public List<DrugFullDTO> listDrugFull(int visitId){
		Sort sort = new Sort(Sort.Direction.ASC, "drugId");
		return drugRepository.findByVisitIdWithMaster(visitId, sort).stream()
		.map(this::resultToDrugFullDTO)
		.collect(Collectors.toList());
	}

	public VisitFullDTO getVisitFull(int visitId){
		VisitDTO visitDTO = getVisit(visitId);
		VisitFullDTO visitFullDTO = new VisitFullDTO();
		visitFullDTO.visit = visitDTO;
		visitFullDTO.shinryouList = listShinryouFull(visitId);
		visitFullDTO.drugs = listDrugFull(visitId);
		return visitFullDTO;
	}

	public ConductDTO getConduct(int conductId){
		Conduct conduct = conductRepository.findOne(conductId);
		return mapper.toConductDTO(conduct);
	}

	public ConductFullDTO getConductFull(int conductId){
		ConductDTO conductDTO = getConduct(conductId);
		return extendConduct(conductDTO);
	}

	private ConductFullDTO extendConduct(ConductDTO conductDTO){
		int conductId = conductDTO.conductId;
		ConductFullDTO conductFullDTO = new ConductFullDTO();
		conductFullDTO.conduct = conductDTO;
		conductFullDTO.gazouLabel = findGazouLabel(conductId);
		conductFullDTO.conductShinryouList = listConductShinryouFull(conductId);
		conductFullDTO.conductDrugs = listConductDrugFull(conductId);
		return conductFullDTO;
	}

	public GazouLabelDTO findGazouLabel(int conductId){
		Optional<GazouLabel> gazouLabel = gazouLabelRepository.findOneByConductId(conductId);
		if( gazouLabel.isPresent() ){
			return mapper.toGazouLabelDTO(gazouLabel.get());
		} else {
			return null;
		}
	}

	public List<ConductShinryouFullDTO> listConductShinryouFull(int conductId){
		return conductShinryouRepository.findByConductIdWithMaster(conductId).stream()
		.map(this::resulToConductShinryouFullDTO)
		.collect(Collectors.toList());
	}

	public List<ConductDrugFullDTO> listConductDrugFull(int conductId){
		return conductDrugRepository.findByConductIdWithMaster(conductId).stream()
		.map(this::resulToConductDrugFullDTO)
		.collect(Collectors.toList());
	}

	private ShinryouFullDTO resultToShinryouFullDTO(Object[] result){
		Shinryou shinryou = (Shinryou)result[0];
		ShinryouMaster master = (ShinryouMaster)result[1];
		ShinryouFullDTO shinryouFullDTO = new ShinryouFullDTO();
		shinryouFullDTO.shinryou = mapper.toShinryouDTO(shinryou);
		shinryouFullDTO.master = mapper.toShinryouMasterDTO(master);
		return shinryouFullDTO;
	}

	private DrugFullDTO resultToDrugFullDTO(Object[] result){
		Drug drug = (Drug)result[0];
		IyakuhinMaster master = (IyakuhinMaster)result[1];
		DrugFullDTO drugFullDTO = new DrugFullDTO();
		drugFullDTO.drug = mapper.toDrugDTO(drug);
		drugFullDTO.master = mapper.toIyakuhinMasterDTO(master);
		return drugFullDTO;
	}

	private ConductShinryouFullDTO resulToConductShinryouFullDTO(Object[] result){
		ConductShinryou conductShinryou = (ConductShinryou)result[0];
		ShinryouMaster master = (ShinryouMaster)result[1];
		ConductShinryouFullDTO conductShinryouFull = new ConductShinryouFullDTO();
		conductShinryouFull.conductShinryou = mapper.toConductShinryouDTO(conductShinryou);
		conductShinryouFull.master = mapper.toShinryouMasterDTO(master);
		return conductShinryouFull;
	}

	private ConductDrugFullDTO resulToConductDrugFullDTO(Object[] result){
		ConductDrug conductDrug = (ConductDrug)result[0];
		IyakuhinMaster master = (IyakuhinMaster)result[1];
		ConductDrugFullDTO conductDrugFull = new ConductDrugFullDTO();
		conductDrugFull.conductDrug = mapper.toConductDrugDTO(conductDrug);
		conductDrugFull.master = mapper.toIyakuhinMasterDTO(master);
		return conductDrugFull;
	}

}
package jp.chang.myclinic.db.myclinic;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	@Autowired
	private PharmaQueueRepository pharmaQueueRepository;
	@Autowired
	private TextRepository textRepository;
	@Autowired
	private PharmaDrugRepository pharmaDrugRepository;
	@Autowired
	private IyakuhinMasterRepository iyakuhinMasterRepository;
	@Autowired
	private HotlineRepository hotlineRepository;
	@Autowired
	private PrescExampleRepository prescExampleRepository;

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

	public List<WqueueFullDTO> listWqueueFullByStates(Set<WqueueWaitState> states){
		if( states.size() == 0 ){
			return Collections.emptyList();
		}
		Set<Integer> waitSets = states.stream().mapToInt(s -> s.getCode()).boxed().collect(Collectors.toSet());
		return wqueueRepository.findFullByStateSet(waitSets).stream()
				.map(this::resultToWqueueFull).collect(Collectors.toList());
	}

	public void enterWqueue(WqueueDTO wqueueDTO){
		Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
		wqueueRepository.save(wqueue);
	}

	public Optional<WqueueDTO> findWqueue(int visitId){
		return wqueueRepository.findByVisitId(visitId).map(mapper::toWqueueDTO);
	}

	public void deleteWqueue(WqueueDTO wqueueDTO){
		Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
		wqueueRepository.delete(wqueue);
	}

	public PatientDTO getPatient(int patientId){
		Patient patient = patientRepository.findOne(patientId);
		return mapper.toPatientDTO(patient);
	}

	public Optional<PatientDTO> findPatient(int patientId){
		return patientRepository.tryFind(patientId).map(mapper::toPatientDTO);
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

	public List<PatientDTO> searchPatient(String text){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		return patientRepository.searchPatient(text, sort).stream()
				.map(mapper::toPatientDTO).collect(Collectors.toList());
	}

	public List<PatientDTO> searchPatient(String textLastName, String textFirstName){
		Sort sort = new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
		return patientRepository.searchPatient(textLastName, textFirstName, sort).stream()
				.map(mapper::toPatientDTO).collect(Collectors.toList());
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
		try(Stream<Shahokokuho> stream = shahokokuhoRepository.findAvailable(patientId, at.toString(), sort)){
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
		String atDate = at.toString();
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
		String atDate = at.toString();
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
		String atDate = at.toString();
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

	public HokenDTO listAvailableHoken(int patientId, String at){
		if( at.length() > 10 ){
			at = at.substring(0, 10);
		}
		LocalDate date = LocalDate.parse(at);
		HokenDTO hokenDTO = new HokenDTO();
		hokenDTO.shahokokuho = findAvailableShahokokuho(patientId, date).stream().findFirst().orElse(null);
		hokenDTO.koukikourei = findAvailableKoukikourei(patientId, date).stream().findFirst().orElse(null);
		hokenDTO.roujin = findAvailableRoujin(patientId, date).stream().findFirst().orElse(null);
		List<KouhiDTO> kouhiList = findAvailableKouhi(patientId, date);
		if( kouhiList.size() > 0 ){
			hokenDTO.kouhi1 = kouhiList.get(0);
			if( kouhiList.size() > 1 ){
				hokenDTO.kouhi2 = kouhiList.get(1);
				if( kouhiList.size() > 2 ){
					hokenDTO.kouhi3 = kouhiList.get(2);
				}
			}
		}
		return hokenDTO;
	}

	public void enterCharge(ChargeDTO chargeDTO){
		Charge charge = mapper.fromChargeDTO(chargeDTO);
		charge = chargeRepository.save(charge);
	}

	public ChargeDTO getCharge(int visitId){
		Charge charge = chargeRepository.findOne(visitId);
		return mapper.toChargeDTO(charge);
	}

	public Optional<ChargeDTO> findCharge(int visitId){
		return chargeRepository.findByVisitId(visitId)
			.map(mapper::toChargeDTO);
	}

	public void enterPayment(PaymentDTO paymentDTO){
		Payment payment = mapper.fromPaymentDTO(paymentDTO);
		paymentRepository.save(payment);
	}

	public List<PaymentDTO> listPayment(int visitId){
		return paymentRepository.findByVisitIdOrderByPaytimeDesc(visitId).stream()
				.map(mapper::toPaymentDTO)
				.collect(Collectors.toList());
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

	public void updateVisit(VisitDTO visitDTO){
		Visit visit = mapper.fromVisitDTO(visitDTO);
		visitRepository.save(visit);
	}

	public List<Integer> listVisitIds(){
		Sort sort = new Sort(Sort.Direction.DESC, "visitId");
		return visitRepository.findAllVisitIds(sort);
	}

	public List<Integer> listVisitIdsForPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "visitId");
		return visitRepository.findVisitIdsByPatient(patientId, sort);
	}

	public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtForPatient(int patientId){
		Sort sort = new Sort(Sort.Direction.DESC, "visitId");
		return visitRepository.findVisitIdVisitedAtByPatient(patientId, sort).stream()
				.map(result -> {
					Integer visitId = (Integer)result[0];
					String visitedAt = (String)result[1];
					VisitIdVisitedAtDTO visitIdVisitedAtDTO = new VisitIdVisitedAtDTO();
					visitIdVisitedAtDTO.visitId = visitId;
					visitIdVisitedAtDTO.visitedAt = visitedAt;
					return visitIdVisitedAtDTO;
				})
				.collect(Collectors.toList());
	}

	public List<VisitPatientDTO> listVisitWithPatient(int page, int itemsPerPage){
		Sort sort = new Sort(Sort.Direction.DESC, "visitId");
		PageRequest pageRequest = new PageRequest(page, itemsPerPage, sort);
		return visitRepository.findAllWithPatient(pageRequest).stream()
			.map(this::resultToVisitPatientDTO)
			.collect(Collectors.toList());
	}

	public ShinryouFullDTO getShinryouFull(int shinryouId){
		Object[] result = shinryouRepository.findOneWithMaster(shinryouId).get(0);
		if( result == null ){
			System.out.println("cannot get full shinryou for shinryouId: " + shinryouId);
		}
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

	public DrugDTO enterDrug(DrugDTO drugDTO){
		Drug drug = mapper.fromDrugDTO(drugDTO);
		drug.setDrugId(null);
		drug = drugRepository.save(drug);
		return mapper.toDrugDTO(drug);
	}

	public void markDrugsAsPrescribedForVisit(int visitId){
		drugRepository.markAsPrescribedForVisit(visitId);
	}

	public List<DrugFullDTO> searchPrevDrug(int patientId){
		List<Integer> drugIds = drugRepository.findNaifukuAndTonpukuPatternByPatient(patientId);
		drugIds.addAll(drugRepository.findGaiyouPatternByPatient(patientId));
		drugIds.sort(Comparator.<Integer>naturalOrder().reversed());
		return drugIds.stream().map(this::getDrugFull).collect(Collectors.toList());
	}

	public List<DrugFullDTO> searchPrevDrug(int patientId, String text){
		List<Integer> drugIds = drugRepository.findNaifukuAndTonpukuPatternByPatient(patientId, text);
		drugIds.addAll(drugRepository.findGaiyouPatternByPatient(patientId, text));
		drugIds.sort(Comparator.<Integer>naturalOrder().reversed());
		return drugIds.stream().map(this::getDrugFull).collect(Collectors.toList());
	}

	public List<TextDTO> listText(int visitId){
		List<Text> texts = textRepository.findByVisitId(visitId);
		return texts.stream().map(mapper::toTextDTO).collect(Collectors.toList());
	}

	public int enterText(TextDTO textDTO){
		Text text = mapper.fromTextDTO(textDTO);
		text.setTextId(null);
		text = textRepository.save(text);
		return text.getTextId();
	}

	public void updateText(TextDTO textDTO){
		Text text = mapper.fromTextDTO(textDTO);
		textRepository.save(text);
	}

	public void deleteText(int textId){
		textRepository.delete(textId);
	}

	public VisitFullDTO getVisitFull(int visitId){
		VisitDTO visitDTO = getVisit(visitId);
		return getVisitFull(visitDTO);
	}

	private VisitFullDTO getVisitFull(Visit visit){
		return getVisitFull(mapper.toVisitDTO(visit));
	}

	private VisitFullDTO getVisitFull(VisitDTO visitDTO){
		int visitId = visitDTO.visitId;
		VisitFullDTO visitFullDTO = new VisitFullDTO();
		visitFullDTO.visit = visitDTO;
		visitFullDTO.texts = listText(visitId);
		visitFullDTO.shinryouList = listShinryouFull(visitId);
		visitFullDTO.drugs = listDrugFull(visitId);
		visitFullDTO.conducts = listConducts(visitId).stream()
				.map(this::extendConduct).collect(Collectors.toList());
		return visitFullDTO;
	}

	private VisitFull2DTO getVisitFull2(Visit visit){
		int visitId = visit.getVisitId();
		VisitDTO visitDTO = mapper.toVisitDTO(visit);
		VisitFull2DTO visitFull2DTO = new VisitFull2DTO();
		visitFull2DTO.visit = visitDTO;
		visitFull2DTO.texts = listText(visitId);
		visitFull2DTO.shinryouList = listShinryouFull(visitId);
		visitFull2DTO.drugs = listDrugFull(visitId);
		visitFull2DTO.conducts = listConducts(visitId).stream()
				.map(this::extendConduct).collect(Collectors.toList());
		visitFull2DTO.hoken = getHokenForVisit(visitDTO);
		visitFull2DTO.charge = findCharge(visitId).orElse(null);
		return visitFull2DTO;
	}

	public VisitFullPageDTO listVisitFull(int patientId, int page){
		int itemsPerPage = 10;
		Pageable pageable = new PageRequest(page, itemsPerPage, Sort.Direction.DESC, "visitId");
		Page<Visit> pageVisit = visitRepository.findByPatientId(patientId, pageable);
		VisitFullPageDTO visitFullPageDTO = new VisitFullPageDTO();
		visitFullPageDTO.totalPages = pageVisit.getTotalPages();
		visitFullPageDTO.page = page;
		visitFullPageDTO.visits = pageVisit.getContent().stream().map(this::getVisitFull).collect(Collectors.toList());
		return visitFullPageDTO;
	}

	public VisitFull2PageDTO listVisitFull2(int patientId, int page){
		int itemsPerPage = 10;
		Pageable pageable = new PageRequest(page, itemsPerPage, Sort.Direction.DESC, "visitId");
		Page<Visit> pageVisit = visitRepository.findByPatientId(patientId, pageable);
		VisitFull2PageDTO visitFull2PageDTO = new VisitFull2PageDTO();
		visitFull2PageDTO.totalPages = pageVisit.getTotalPages();
		visitFull2PageDTO.page = page;
		visitFull2PageDTO.visits = pageVisit.getContent().stream().map(this::getVisitFull2).collect(Collectors.toList());
		return visitFull2PageDTO;
	}

	public ConductDTO getConduct(int conductId){
		Conduct conduct = conductRepository.findOne(conductId);
		return mapper.toConductDTO(conduct);
	}

	public ConductFullDTO getConductFull(int conductId){
		ConductDTO conductDTO = getConduct(conductId);
		return extendConduct(conductDTO);
	}

	public void deleteVisitFromReception(int visitId){
		Optional<Wqueue> wqueueOpt = wqueueRepository.findByVisitId(visitId);
		if( wqueueOpt.isPresent() ){
			Wqueue wqueue = wqueueOpt.get();
			if( wqueue.getWaitState() != WqueueWaitState.WaitExam.getCode() ){
				throw new RuntimeException("診察の状態が診察待ちでないため、削除できません。");
			}
		}
		deleteVisitSafely(visitId);
	}

	public void deleteVisitSafely(int visitId){
		VisitFullDTO visit = getVisitFull(visitId);
		if( visit.texts.size() > 0 ){
			throw new RuntimeException("文章があるので、診察を削除できません。");
		}
		if( visit.drugs.size() > 0 ){
			throw new RuntimeException("投薬があるので、診察を削除できません。");
		}
		if( visit.shinryouList.size() > 0 ){
			throw new RuntimeException("診療行為があるので、診察を削除できません。");
		}
		if( visit.conducts.size() > 0 ){
			throw new RuntimeException("処置があるので、診察を削除できません。");
		}
		Optional<Charge> optCharge = chargeRepository.findByVisitId(visitId);
		if( optCharge.isPresent() ){
			throw new RuntimeException("請求があるので、診察を削除できません。");
		}
		List<Payment> payments = paymentRepository.findByVisitId(visitId);
		if( payments.size() > 0 ){
			throw new RuntimeException("支払い記録があるので、診察を削除できません。");
		}
		Optional<Wqueue> optWqueue = wqueueRepository.findByVisitId(visitId);
		if( optWqueue.isPresent() ){
			wqueueRepository.delete(optWqueue.get());
		}
		Optional<PharmaQueue> optPharmaQueue = pharmaQueueRepository.findByVisitId(visitId);
		if( optPharmaQueue.isPresent() ){
			pharmaQueueRepository.delete(optPharmaQueue.get());
		}
		visitRepository.delete(visitId);
	}

	private ConductFullDTO extendConduct(ConductDTO conductDTO){
		int conductId = conductDTO.conductId;
		ConductFullDTO conductFullDTO = new ConductFullDTO();
		conductFullDTO.conduct = conductDTO;
		conductFullDTO.gazouLabel = findGazouLabel(conductId);
		conductFullDTO.conductShinryouList = listConductShinryouFull(conductId);
		conductFullDTO.conductDrugs = listConductDrugFull(conductId);
		conductFullDTO.conductKizaiList = listConductKizaiFull(conductId);
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

	public List<ConductDTO> listConducts(int visitId){
		return conductRepository.findByVisitId(visitId).stream()
			.map(mapper::toConductDTO)
			.collect(Collectors.toList());
	}

	public List<ConductFullDTO> listConductFull(int visitId){
		return listConducts(visitId).stream()
			.map(this::extendConduct)
			.collect(Collectors.toList());
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

	public List<ConductKizaiFullDTO> listConductKizaiFull(int conductId){
		return conductKizaiRepository.findByConductIdWithMaster(conductId).stream()
		.map(this::resulToConductKizaiFullDTO)
		.collect(Collectors.toList());
	}

	public HokenDTO getHokenForVisit(VisitDTO visitDTO){
		HokenDTO hokenDTO = new HokenDTO();
		if( visitDTO.shahokokuhoId > 0 ){
			hokenDTO.shahokokuho = mapper.toShahokokuhoDTO(shahokokuhoRepository.findOne(visitDTO.shahokokuhoId));
		}
		if( visitDTO.koukikoureiId > 0 ){
			hokenDTO.koukikourei = mapper.toKoukikoureiDTO(koukikoureiRepository.findOne(visitDTO.koukikoureiId));
		}
		if( visitDTO.roujinId > 0 ){
			hokenDTO.roujin = mapper.toRoujinDTO(roujinRepository.findOne(visitDTO.roujinId));
		}
		if( visitDTO.kouhi1Id > 0 ){
			hokenDTO.kouhi1 = mapper.toKouhiDTO(kouhiRepository.findOne(visitDTO.kouhi1Id));
		}
		if( visitDTO.kouhi2Id > 0 ){
			hokenDTO.kouhi2 = mapper.toKouhiDTO(kouhiRepository.findOne(visitDTO.kouhi2Id));
		}
		if( visitDTO.kouhi3Id > 0 ){
			hokenDTO.kouhi3 = mapper.toKouhiDTO(kouhiRepository.findOne(visitDTO.kouhi3Id));
		}
		return hokenDTO;
	}

	public List<PaymentVisitPatientDTO> listRecentPayment(int n) {
		PageRequest pageRequest = new PageRequest(0, n, Sort.Direction.DESC, "visitId");
		List<Integer> visitIds = paymentRepository.findFinalPayment(pageRequest).stream()
				.map(payment -> payment.getVisitId()).collect(Collectors.toList());
		if( visitIds.isEmpty() ){
			return Collections.emptyList();
		}
		return paymentRepository.findFullFinalPayment(visitIds, pageRequest).stream()
				.map(this::resultToPaymentVisitPatient).collect(Collectors.toList());
	}

	public List<PaymentVisitPatientDTO> listPaymentByPatient(int patientId, int n) {
		PageRequest pageRequest = new PageRequest(0, n, Sort.Direction.DESC, "visitId");
		return paymentRepository.findFullByPatient(patientId, pageRequest).getContent().stream()
				.map(this::resultToPaymentVisitPatient).collect(Collectors.toList());
	}

	public List<PaymentDTO> listFinalPayment(int n){
		PageRequest pageRequest = new PageRequest(0, n, Sort.Direction.DESC, "visitId");
		return paymentRepository.findFinalPayment(pageRequest).stream()
				.map(mapper::toPaymentDTO).collect(Collectors.toList());
	}

	public void finishCashier(PaymentDTO paymentDTO){
		Payment payment = mapper.fromPaymentDTO(paymentDTO);
		paymentRepository.save(payment);
		Optional<PharmaQueue> optPharmaQueue = pharmaQueueRepository.findByVisitId(paymentDTO.visitId);
		Optional<Wqueue> optWqueue = wqueueRepository.findByVisitId(paymentDTO.visitId);
		if( optPharmaQueue.isPresent() ){
			if( optWqueue.isPresent() ){
				Wqueue wqueue = optWqueue.get();
				wqueue.setWaitState(WqueueWaitState.WaitDrug.getCode());
				wqueueRepository.save(wqueue);
			}
		} else {
			if( optWqueue.isPresent() ){
				Wqueue wqueue = optWqueue.get();
				wqueueRepository.delete(wqueue);
			}
		}
	}

	public Optional<PharmaQueueDTO> findPharmaQueue(int visitId){
		return pharmaQueueRepository.findByVisitId(visitId).map(mapper::toPharmaQueueDTO);
	}

	public List<PharmaQueueFullDTO> listPharmaQueueFullForPrescription(){
		return pharmaQueueRepository.findFull().stream()
				.map(result -> {
					PharmaQueueFullDTO pharmaQueueFullDTO = new PharmaQueueFullDTO();
					pharmaQueueFullDTO.pharmaQueue = mapper.toPharmaQueueDTO((PharmaQueue)result[0]);
					pharmaQueueFullDTO.patient = mapper.toPatientDTO((Patient)result[1]);
					Optional<Wqueue> optWqueue = wqueueRepository.findByVisitId(pharmaQueueFullDTO.pharmaQueue.visitId);
					pharmaQueueFullDTO.wqueue = optWqueue.map(mapper::toWqueueDTO).orElse(null);
					pharmaQueueFullDTO.visitId = pharmaQueueFullDTO.pharmaQueue.visitId;
					return pharmaQueueFullDTO;
				})
				.collect(Collectors.toList());
	}

	public List<PharmaQueueFullDTO> listPharmaQueueFullForToday(){
		List<Integer> visitIds = visitRepository.findVisitIdForToday();
		if( visitIds.isEmpty() ){
			return Collections.emptyList();
		}
		Map<Integer, WqueueDTO> wqueueMap = new HashMap<>();
		Map<Integer, PharmaQueueDTO> pharmaQueueMap = new HashMap<>();
		wqueueRepository.findAll().forEach(wq -> {
			wqueueMap.put(wq.getVisitId(), mapper.toWqueueDTO(wq));
		});
		pharmaQueueRepository.findAll().forEach(pq -> {
			pharmaQueueMap.put(pq.getVisitId(), mapper.toPharmaQueueDTO(pq));
		});
		return visitRepository.findByVisitIdsWithPatient(visitIds).stream()
				.map(result -> {
					Visit visit = (Visit)result[0];
					int visitId = visit.getVisitId();
					Patient patient = (Patient)result[1];
					PharmaQueueFullDTO pharmaQueueFullDTO = new PharmaQueueFullDTO();
					pharmaQueueFullDTO.visitId = visitId;
					pharmaQueueFullDTO.patient = mapper.toPatientDTO(patient);
					pharmaQueueFullDTO.pharmaQueue = pharmaQueueMap.get(visitId);
					pharmaQueueFullDTO.wqueue = wqueueMap.get(visitId);
					return pharmaQueueFullDTO;
				})
				.collect(Collectors.toList());
	}

	public void deletePharmaQueue(PharmaQueueDTO pharmaQueueDTO){
		PharmaQueue pharmaQueue = mapper.fromPharmaQueueDTO(pharmaQueueDTO);
		pharmaQueueRepository.delete(pharmaQueue);
	}

	public PharmaDrugDTO getPharmaDrugByIyakuhincode(int iyakuhincode){
		return mapper.toPharmaDrugDTO(pharmaDrugRepository.findByIyakuhincode(iyakuhincode));
	}

	public Optional<PharmaDrugDTO> findPharmaDrugByIyakuhincode(int iyakuhincode){
		return pharmaDrugRepository.tryFindByIyakuhincode(iyakuhincode)
				.map(mapper::toPharmaDrugDTO);
	}

	public List<PharmaDrugDTO> collectPharmaDrugByIyakuhincodes(List<Integer> iyakuhincodes){
		if( iyakuhincodes.size() == 0 ) {
			return Collections.emptyList();
		} else {
			return pharmaDrugRepository.collectByIyakuhincodes(iyakuhincodes).stream()
					.map(mapper::toPharmaDrugDTO).collect(Collectors.toList());
		}
	}

	public void enterPharmaDrug(PharmaDrugDTO pharmaDrugDTO){
	    PharmaDrug pharmaDrug = mapper.fromPharmaDrugDTO(pharmaDrugDTO);
	    pharmaDrugRepository.save(pharmaDrug);
    }

	public void updatePharmaDrug(PharmaDrugDTO pharmaDrugDTO){
	    PharmaDrug pharmaDrug = mapper.fromPharmaDrugDTO(pharmaDrugDTO);
	    pharmaDrugRepository.save(pharmaDrug);
    }

    public void deletePharmaDrug(int iyakuhincode){
	    pharmaDrugRepository.delete(iyakuhincode);
    }

    public List<PharmaDrugNameDTO> searchPharmaDrugNames(String text){
		return pharmaDrugRepository.searchNames(text).stream()
				.map(result -> {
					PharmaDrugNameDTO pharmaDrugNameDTO = new PharmaDrugNameDTO();
					pharmaDrugNameDTO.iyakuhincode = (Integer)result[0];
					pharmaDrugNameDTO.name = (String)result[1];
					pharmaDrugNameDTO.yomi = (String)result[2];
					return pharmaDrugNameDTO;
				})
                .sorted(Comparator.comparing(a -> a.yomi))
				.collect(Collectors.toList());
	}

	public List<PharmaDrugNameDTO> listAllPharmaDrugNames(){
    	return pharmaDrugRepository.findAllPharmaDrugNames().stream()
				.map(result -> {
					PharmaDrugNameDTO pharmaDrugNameDTO = new PharmaDrugNameDTO();
					pharmaDrugNameDTO.iyakuhincode = (Integer)result[0];
					pharmaDrugNameDTO.name = (String)result[1];
					pharmaDrugNameDTO.yomi = (String)result[2];
					return pharmaDrugNameDTO;
				})
				.sorted(Comparator.comparing(a -> a.yomi))
				.collect(Collectors.toList());
	}

	public List<VisitTextDrugDTO> listVisitTextDrug(List<Integer> visitIds){
		if( visitIds.size() == 0 ){
			return Collections.emptyList();
		}
		return visitRepository.findByVisitIds(visitIds, new Sort(Sort.Direction.DESC, "visitId")).stream()
				.map(visit -> {
					VisitTextDrugDTO visitTextDrugDTO = new VisitTextDrugDTO();
					visitTextDrugDTO.visit = mapper.toVisitDTO(visit);
					visitTextDrugDTO.texts = listText(visit.getVisitId());
					visitTextDrugDTO.drugs = listDrugFull(visit.getVisitId());
					return visitTextDrugDTO;
				})
				.collect(Collectors.toList());
	}

	public List<Integer> listIyakuhincodeForPatient(int patientId){
		return drugRepository.findIyakuhincodeByPatient(patientId);
	}

	public List<IyakuhincodeNameDTO> findNamesForIyakuhincodes(List<Integer> iyakuhincodes){
		if( iyakuhincodes.size() == 0 ){
			return Collections.emptyList();
		}
		return iyakuhinMasterRepository.findNameForIyakuhincode(iyakuhincodes, new Sort(Sort.Direction.ASC, "yomi")).stream()
				.map(result -> {
					Integer iyakuhincode = (Integer)result[0];
					String name = (String)result[1];
					IyakuhincodeNameDTO iyakuhincodeNameDTO = new IyakuhincodeNameDTO();
					iyakuhincodeNameDTO.iyakuhincode = iyakuhincode;
					iyakuhincodeNameDTO.name = name;
					return iyakuhincodeNameDTO;
				})
				.collect(Collectors.toList());
	}

	public List<IyakuhinMasterDTO> searchIyakuhinByName(String text, LocalDate at){
		return iyakuhinMasterRepository.searchByName(text, at.toString(), new Sort("yomi")).stream()
				.map(mapper::toIyakuhinMasterDTO).collect(Collectors.toList());
	}

	public List<IyakuhincodeNameDTO> listIyakuhinForPatient(int patientId){
		List<Integer> iyakuhincodes = listIyakuhincodeForPatient(patientId);
		return findNamesForIyakuhincodes(iyakuhincodes);
	}

	public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByIyakuhincodeAndPatientId(int patientId, int iyakuhincode){
		return drugRepository.findVisitIdVisitedAtByPatientAndIyakuhincode(patientId, iyakuhincode).stream()
				.map(result -> {
					VisitIdVisitedAtDTO visitIdVisitedAtDTO = new VisitIdVisitedAtDTO();
					visitIdVisitedAtDTO.visitId = (Integer)result[0];
					visitIdVisitedAtDTO.visitedAt = (String)result[1];
					return visitIdVisitedAtDTO;
				})
				.collect(Collectors.toList());
	}

	public Optional<IyakuhinMasterDTO> findIyakuhinMaster(int iyakuhincode, String at){
		return iyakuhinMasterRepository.tryFind(iyakuhincode, at).map(mapper::toIyakuhinMasterDTO);
	}

	public Integer getLastHotlineId(){
		Optional<Hotline> hotline = hotlineRepository.findTopByOrderByHotlineIdDesc();
		return hotline.map(Hotline::getHotlineId).orElse(0);
	}

	public List<HotlineDTO> listHotlineInRange(int lowerHotlineId, int upperHotlineId){
		Sort sort = new Sort(Sort.Direction.ASC, "hotlineId");
		return hotlineRepository.findInRange(lowerHotlineId, upperHotlineId, sort).stream()
				.map(mapper::toHotlineDTO).collect(Collectors.toList());
	}

	public List<HotlineDTO> listRecentHotline(int thresholdHotlineId){
		return hotlineRepository.findRecent(thresholdHotlineId).stream()
				.map(mapper::toHotlineDTO).collect(Collectors.toList());
	}

	public int enterHotline(HotlineDTO hotlineDTO){
		Hotline hotline = mapper.fromHotlineDTO(hotlineDTO);
		hotline = hotlineRepository.save(hotline);
		return hotline.getHotlineId();
	}

	public List<HotlineDTO> listTodaysHotline(){
		return hotlineRepository.findTodaysHotline().stream()
				.map(mapper::toHotlineDTO)
				.collect(Collectors.toList());
	}

	public List<PrescExampleFullDTO> searchPrescExampleFullByName(String text){
		return prescExampleRepository.searchByNameFull(text).stream()
				.map(result -> {
					PrescExample prescExample = (PrescExample)result[0];
					IyakuhinMaster iyakuhinMaster = (IyakuhinMaster)result[1];
					PrescExampleFullDTO prescExampleFullDTO = new PrescExampleFullDTO();
					prescExampleFullDTO.prescExample = mapper.toPrescExampleDTO((prescExample));
					prescExampleFullDTO.master = mapper.toIyakuhinMasterDTO(iyakuhinMaster);
					return prescExampleFullDTO;
				})
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

	private ConductKizaiFullDTO resulToConductKizaiFullDTO(Object[] result){
		ConductKizai conductKizai = (ConductKizai)result[0];
		KizaiMaster master = (KizaiMaster)result[1];
		ConductKizaiFullDTO conductKizaiFull = new ConductKizaiFullDTO();
		conductKizaiFull.conductKizai = mapper.toConductKizaiDTO(conductKizai);
		conductKizaiFull.master = mapper.toKizaiMasterDTO(master);
		return conductKizaiFull;
	}

	private VisitPatientDTO resultToVisitPatientDTO(Object[] result){
		VisitDTO visitDTO = mapper.toVisitDTO((Visit)result[0]);
		PatientDTO patientDTO = mapper.toPatientDTO((Patient)result[1]);
		VisitPatientDTO visitPatientDTO = new VisitPatientDTO();
		visitPatientDTO.visit = visitDTO;
		visitPatientDTO.patient = patientDTO;
		return visitPatientDTO;
	}

	private PaymentVisitPatientDTO resultToPaymentVisitPatient(Object[] result){
		PaymentDTO paymentDTO = mapper.toPaymentDTO((Payment)result[0]);
		VisitDTO visitDTO = mapper.toVisitDTO((Visit)result[1]);
		PatientDTO patientDTO = mapper.toPatientDTO((Patient)result[2]);
		PaymentVisitPatientDTO paymentVisitPatientDTO = new PaymentVisitPatientDTO();
		paymentVisitPatientDTO.payment = paymentDTO;
		paymentVisitPatientDTO.visit = visitDTO;
		paymentVisitPatientDTO.patient = patientDTO;
		return paymentVisitPatientDTO;
	}

	private PharmaQueueFullDTO resultToPharmaQueueFull(Object[] result){
		PharmaQueueFullDTO pharmaQueueFullDTO = new PharmaQueueFullDTO();
		PatientDTO patientDTO = mapper.toPatientDTO((Patient)result[1]);
		pharmaQueueFullDTO.pharmaQueue = mapper.toPharmaQueueDTO((PharmaQueue)result[0]);
		pharmaQueueFullDTO.patient = mapper.toPatientDTO((Patient)result[1]);
		return pharmaQueueFullDTO;
	}

	private WqueueFullDTO resultToWqueueFull(Object[] result){
		WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
		wqueueFullDTO.wqueue = mapper.toWqueueDTO((Wqueue)result[0]);
		wqueueFullDTO.patient = mapper.toPatientDTO((Patient)result[1]);
		wqueueFullDTO.visit = mapper.toVisitDTO((Visit)result[2]);
		return wqueueFullDTO;
	}

 }
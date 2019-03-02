package jp.chang.myclinic.server.db.myclinic;

import jp.chang.myclinic.consts.PharmaQueueState;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.server.HotlineLogger;
import jp.chang.myclinic.server.PracticeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DbGateway implements DbGatewayInterface {
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
    private ShinryouMasterRepository shinryouMasterRepository;
    @Autowired
    private KizaiMasterRepository kizaiMasterRepository;
    @Autowired
    private HotlineRepository hotlineRepository;
    @Autowired
    private PrescExampleRepository prescExampleRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DiseaseAdjRepository diseaseAdjRepository;
    @Autowired
    private ByoumeiMasterRepository byoumeiMasterRepository;
    @Autowired
    private ShuushokugoMasterRepository shuushokugoMasterRepository;
    @Autowired
    private PracticeLogRepository practiceLogRepository;
    @Autowired
    private PracticeLogger practiceLogger;
    @Autowired
    private HotlineLogger hotlineLogger;
    @Autowired
    private ShinryouAttrRepository shinryouAttrRepository;
    @Autowired
    private DrugAttrRepository drugAttrRepository;
    @Autowired
    private ShoukiRepository shoukiRepository;

    private WqueueFullDTO composeWqueueFullDTO(Wqueue wqueue){
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = mapper.toWqueueDTO(wqueue);
        wqueueFullDTO.visit = mapper.toVisitDTO(wqueue.getVisit());
        wqueueFullDTO.patient = mapper.toPatientDTO(wqueue.getVisit().getPatient());
        return wqueueFullDTO;
    }

    @Override
    public List<WqueueFullDTO> listWqueueFull() {
        try (Stream<Wqueue> stream = wqueueRepository.findAllAsStream()) {
            return stream.map(this::composeWqueueFullDTO).collect(Collectors.toList());
        }
    }

    @Override
    public WqueueFullDTO getWqueueFull(int visitId) {
        Wqueue wqueue = wqueueRepository.findOneByVisitId(visitId);
        return composeWqueueFullDTO(wqueue);
    }

    @Override
    public List<WqueueFullDTO> listWqueueFullByStates(Set<WqueueWaitState> states) {
        if (states.size() == 0) {
            return Collections.emptyList();
        }
        Set<Integer> waitSets = states.stream().mapToInt(WqueueWaitState::getCode).boxed().collect(Collectors.toSet());
        return wqueueRepository.findFullByStateSet(waitSets, Sort.by(Sort.Direction.ASC, "visitId")).stream()
                .map(this::resultToWqueueFull).collect(Collectors.toList());
    }

    @Override
    public List<WqueueDTO> listWqueueByStates(Set<WqueueWaitState> states, Sort sort) {
        Set<Integer> waitSets = states.stream().mapToInt(WqueueWaitState::getCode).boxed().collect(Collectors.toSet());
        return wqueueRepository.findByStateSet(waitSets, sort).stream()
                .map(mapper::toWqueueDTO).collect(Collectors.toList());
    }

    @Override
    public List<VisitPatientDTO> listTodaysVisits() {
        List<Integer> visitIds = visitRepository.findVisitIdForToday(Sort.by("visitId"));
        if (visitIds.size() == 0) {
            return Collections.emptyList();
        }
        return visitRepository.findWithPatient(visitIds, Sort.by("visitId")).stream()
                .map(this::resultToVisitPatientDTO).collect(Collectors.toList());
    }

    private List<VisitFull2PatientDTO> convertToVisitFull2Patients(List<Integer> visitIds) {
        if (visitIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<VisitPatientDTO> visitPatients = visitRepository
                    .findByVisitIdsWithPatient(visitIds, Sort.by(Sort.Direction.DESC, "visitId"))
                    .stream()
                    .map(this::resultToVisitPatientDTO).collect(Collectors.toList());
            return visitPatients.stream()
                    .map(visitPatient -> {
                        VisitFull2DTO visitFull = getVisitFull2(mapper.fromVisitDTO(visitPatient.visit));
                        VisitFull2PatientDTO result = new VisitFull2PatientDTO();
                        result.patient = visitPatient.patient;
                        result.visitFull = visitFull;
                        return result;
                    }).collect(Collectors.toList());
        }
    }

    @Override
    public VisitFull2PatientPageDTO pageVisitsWithPatientAt(LocalDate date, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "visitId");
        Page<Integer> pageVisitIds = visitRepository.pageVisitIdAt(date.toString(), pageRequest);
        List<VisitFull2PatientDTO> visitFullPatients = convertToVisitFull2Patients(pageVisitIds.getContent());
        VisitFull2PatientPageDTO resultPage = new VisitFull2PatientPageDTO();
        resultPage.page = page;
        resultPage.totalPages = pageVisitIds.getTotalPages();
        resultPage.visitPatients = visitFullPatients;
        return resultPage;
    }

    @Override
    public List<VisitFull2PatientDTO> listVisitFull2PatientOfToday() {
        List<Integer> visitIds = visitRepository.findVisitIdForToday(Sort.by(Sort.Direction.DESC, "visitId"));
        return convertToVisitFull2Patients(visitIds);
    }

    @Override
    public void enterWqueue(WqueueDTO wqueueDTO) {
        Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
        wqueueRepository.save(wqueue);
        practiceLogger.logWqueueCreated(wqueueDTO);
    }

    @Override
    public Optional<WqueueDTO> findWqueue(int visitId) {
        return wqueueRepository.tryFindByVisitId(visitId).map(mapper::toWqueueDTO);
    }

    @Override
    public void deleteWqueue(WqueueDTO wqueueDTO) {
        Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
        wqueueRepository.delete(wqueue);
        practiceLogger.logWqueueDeleted(wqueueDTO);
    }

    private void changeWqueueState(int visitId, int state) {
        Wqueue wqueue = wqueueRepository.findOneByVisitId(visitId);
        WqueueDTO prev = mapper.toWqueueDTO(wqueue);
        wqueue.setWaitState(state);
        wqueue = wqueueRepository.save(wqueue);
        WqueueDTO updated = mapper.toWqueueDTO(wqueue);
        practiceLogger.logWqueueUpdated(prev, updated);
    }

    @Override
    public void startExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.InExam.getCode());
    }

    @Override
    public void suspendExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.WaitReExam.getCode());
    }

    private boolean isTodaysVisit(Visit visit){
        return visit.getVisitedAt().substring(0, 10).equals(LocalDate.now().toString());
    }

    @Override
    public void endExam(int visitId, int charge) {
        Visit visit = visitRepository.findById(visitId);
        boolean isToday = isTodaysVisit(visit);
        setChargeOfVisit(visitId, charge);
        Wqueue wqueue = wqueueRepository.tryFindByVisitId(visitId).orElse(null);
        if (wqueue != null && isToday) {
            changeWqueueState(visitId, WqueueWaitState.WaitCashier.getCode());
        } else {
            if(wqueue != null ){ // it not today
                deleteWqueue(mapper.toWqueueDTO(wqueue));
            }
            Wqueue newWqueue = new Wqueue();
            newWqueue.setVisitId(visitId);
            newWqueue.setWaitState(WqueueWaitState.WaitCashier.getCode());
            enterWqueue(mapper.toWqueueDTO(newWqueue));
        }
        pharmaQueueRepository.findByVisitId(visitId).ifPresent(pharmaQueue -> {
            PharmaQueueDTO deleted = mapper.toPharmaQueueDTO(pharmaQueue);
            pharmaQueueRepository.deleteByVisitId(visitId);
            practiceLogger.logPharmaQueueDeleted(deleted);
        });
        if (isToday) {
            int unprescribed = drugRepository.countByVisitIdAndPrescribed(visitId, 0);
            if (unprescribed > 0) {
                PharmaQueue pharmaQueue = new PharmaQueue();
                pharmaQueue.setVisitId(visitId);
                pharmaQueue.setPharmaState(PharmaQueueState.WaitPack.getCode());
                pharmaQueueRepository.save(pharmaQueue);
                practiceLogger.logPharmaQueueCreated(mapper.toPharmaQueueDTO(pharmaQueue));
            }
        }
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new RuntimeException("患者情報の取得に失敗しました。");
        }
        return mapper.toPatientDTO(patient);
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        return patientRepository.tryFind(patientId).map(mapper::toPatientDTO);
    }

    @Override
    public int enterPatient(PatientDTO patientDTO) {
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        practiceLogger.logPatientCreated(mapper.toPatientDTO(patient));
        return patient.getPatientId();
    }

    @Override
    public void updatePatient(PatientDTO patientDTO) {
        PatientDTO prev = getPatient(patientDTO.patientId);
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        practiceLogger.logPatientUpdated(prev, mapper.toPatientDTO(patient));
    }

    @Override
    public List<PatientDTO> searchPatientByLastName(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByLastNameContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatientByFirstName(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByFirstNameContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatientByLastNameYomi(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByLastNameYomiContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatientByFirstNameYomi(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByFirstNameYomiContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatientByName(String lastName, String firstName) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.searchPatientByName(lastName, firstName, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatientByYomi(String lastNameYomi, String firstNameYomi) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.searchPatientByYomi(lastNameYomi, firstNameYomi, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    @Override
    public List<PatientDTO> searchPatient(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        return patientRepository.searchPatient(text, sort).stream()
                .map(mapper::toPatientDTO).collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchPatient(String textLastName, String textFirstName) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        return patientRepository.searchPatient(textLastName, textFirstName, sort).stream()
                .map(mapper::toPatientDTO).collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> listRecentlyRegisteredPatients(int n) {
        PageRequest pageRequest = PageRequest.of(0, n, Sort.Direction.DESC, "patientId");
        return patientRepository.findAll(pageRequest).map(mapper::toPatientDTO).getContent();
    }

    @Override
    public int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
        shahokokuho = shahokokuhoRepository.save(shahokokuho);
        practiceLogger.logShahokokuhoCreated(mapper.toShahokokuhoDTO(shahokokuho));
        return shahokokuho.getShahokokuhoId();
    }

    @Override
    public void updateShahokokuho(ShahokokuhoDTO shahokokuhoDTO){
        ShahokokuhoDTO prev = getShahokokuho(shahokokuhoDTO.shahokokuhoId);
        Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
        shahokokuhoRepository.save(shahokokuho);
        practiceLogger.logShahokokuhoUpdated(prev, shahokokuhoDTO);
    }

    @Override
    public void deleteShahokokuho(int shahokokuhoId) {
        int usage = visitRepository.countByShahokokuhoId(shahokokuhoId);
        if (usage != 0) {
            throw new RuntimeException("この社保・国保はすでに使用されているので、削除できません。");
        }
        ShahokokuhoDTO deleted = mapper.toShahokokuhoDTO(shahokokuhoRepository.findById(shahokokuhoId));
        shahokokuhoRepository.deleteById(shahokokuhoId);
        practiceLogger.logShahokokuhoDeleted(deleted);
    }

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "shahokokuhoId");
        try (Stream<Shahokokuho> stream = shahokokuhoRepository.findAvailable(patientId, at.toString(), sort)) {
            return stream.map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
        }
    }

    private List<ShahokokuhoDTO> findShahokokuhoByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "shahokokuhoId");
        return shahokokuhoRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return mapper.toShahokokuhoDTO(shahokokuhoRepository.findById(shahokokuhoId));
    }

    @Override
    public int enterKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
        koukikourei = koukikoureiRepository.save(koukikourei);
        practiceLogger.logKoukikoureiCreated(mapper.toKoukikoureiDTO(koukikourei));
        return koukikourei.getKoukikoureiId();
    }

    @Override
    public void updateKoukikourei(KoukikoureiDTO koukikoureiDTO){
        KoukikoureiDTO prev = getKoukikourei(koukikoureiDTO.koukikoureiId);
        Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
        koukikoureiRepository.save(koukikourei);
        practiceLogger.logKoukikoureiUpdated(prev, koukikoureiDTO);
    }

    @Override
    public void deleteKoukikourei(int koukikoureiId) {
        if (visitRepository.countByKoukikoureiId(koukikoureiId) > 0) {
            throw new RuntimeException("この後期高齢保険はすでに使用されているので、削除できません。");
        }
        KoukikoureiDTO deleted = mapper.toKoukikoureiDTO(koukikoureiRepository.findById(koukikoureiId));
        koukikoureiRepository.deleteById(koukikoureiId);
        practiceLogger.logKoukikoureiDeleted(deleted);
    }

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "koukikoureiId");
        String atDate = at.toString();
        try (Stream<Koukikourei> stream = koukikoureiRepository.findAvailable(patientId, atDate, sort)) {
            return stream.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
        }
    }

    private List<KoukikoureiDTO> findKoukikoureiByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "koukikoureiId");
        return koukikoureiRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
    }

    @Override
    public int enterRoujin(RoujinDTO roujinDTO) {
        Roujin roujin = mapper.fromRoujinDTO(roujinDTO);
        roujin = roujinRepository.save(roujin);
        practiceLogger.logRoujinCreated(mapper.toRoujinDTO(roujin));
        return roujin.getRoujinId();
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return mapper.toKoukikoureiDTO(koukikoureiRepository.findById(koukikoureiId));
    }

    @Override
    public void deleteRoujin(int roujinId) {
        if (visitRepository.countByRoujinId(roujinId) > 0) {
            throw new RuntimeException("この老人保険はすでに使用されているので、削除できません。");
        }
        RoujinDTO deleted = mapper.toRoujinDTO(roujinRepository.findById(roujinId));
        roujinRepository.deleteById(roujinId);
        practiceLogger.logRoujinDeleted(deleted);
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "roujinId");
        String atDate = at.toString();
        try (Stream<Roujin> stream = roujinRepository.findAvailable(patientId, atDate, sort)) {
            return stream.map(mapper::toRoujinDTO).collect(Collectors.toList());
        }
    }

    private List<RoujinDTO> findRoujinByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "roujinId");
        return roujinRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toRoujinDTO).collect(Collectors.toList());
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        return mapper.toRoujinDTO(roujinRepository.findById(roujinId));
    }

    @Override
    public int enterKouhi(KouhiDTO kouhiDTO) {
        Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
        kouhi = kouhiRepository.save(kouhi);
        practiceLogger.logKouhiCreated(mapper.toKouhiDTO(kouhi));
        return kouhi.getKouhiId();
    }

    @Override
    public void updateKouhi(KouhiDTO kouhiDTO){
        KouhiDTO prev = getKouhi(kouhiDTO.kouhiId);
        Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
        kouhiRepository.save(kouhi);
        practiceLogger.logKouhiUpdated(prev, kouhiDTO);
    }

    @Override
    public void deleteKouhi(int kouhiId) {
        if (visitRepository.countByKouhi1IdOrKouhi2IdOrKouhi3Id(kouhiId, kouhiId, kouhiId) > 0) {
            throw new RuntimeException("この公費負担はすでに使われているので、削除できません。");
        }
        KouhiDTO deleted = mapper.toKouhiDTO(kouhiRepository.findById(kouhiId));
        kouhiRepository.deleteById(kouhiId);
        practiceLogger.logKouhiDeleted(deleted);
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.ASC, "kouhiId");
        String atDate = at.toString();
        try (Stream<Kouhi> stream = kouhiRepository.findAvailable(patientId, atDate, sort)) {
            return stream.map(mapper::toKouhiDTO).collect(Collectors.toList());
        }
    }

    private List<KouhiDTO> findKouhiByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "kouhiId");
        return kouhiRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toKouhiDTO).collect(Collectors.toList());
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return mapper.toKouhiDTO(kouhiRepository.findById(kouhiId));
    }

    @Override
    public HokenListDTO findHokenByPatient(int patientId) {
        HokenListDTO hokenListDTO = new HokenListDTO();
        hokenListDTO.shahokokuhoListDTO = findShahokokuhoByPatient(patientId);
        hokenListDTO.koukikoureiListDTO = findKoukikoureiByPatient(patientId);
        hokenListDTO.roujinListDTO = findRoujinByPatient(patientId);
        hokenListDTO.kouhiListDTO = findKouhiByPatient(patientId);
        return hokenListDTO;
    }

    @Override
    public HokenDTO listAvailableHoken(int patientId, String at) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        LocalDate date = LocalDate.parse(at);
        HokenDTO hokenDTO = new HokenDTO();
        hokenDTO.shahokokuho = findAvailableShahokokuho(patientId, date).stream().findFirst().orElse(null);
        hokenDTO.koukikourei = findAvailableKoukikourei(patientId, date).stream().findFirst().orElse(null);
        hokenDTO.roujin = findAvailableRoujin(patientId, date).stream().findFirst().orElse(null);
        List<KouhiDTO> kouhiList = findAvailableKouhi(patientId, date);
        if (kouhiList.size() > 0) {
            hokenDTO.kouhi1 = kouhiList.get(0);
            if (kouhiList.size() > 1) {
                hokenDTO.kouhi2 = kouhiList.get(1);
                if (kouhiList.size() > 2) {
                    hokenDTO.kouhi3 = kouhiList.get(2);
                }
            }
        }
        return hokenDTO;
    }

    @Override
    public void enterCharge(ChargeDTO chargeDTO) {
        Charge charge = mapper.fromChargeDTO(chargeDTO);
        charge = chargeRepository.save(charge);
        practiceLogger.logChargeCreated(mapper.toChargeDTO(charge));
    }

    @Override
    public ChargeDTO getCharge(int visitId) {
        Charge charge = chargeRepository.findById(visitId);
        return mapper.toChargeDTO(charge);
    }

    @Override
    public Optional<ChargeDTO> findCharge(int visitId) {
        return chargeRepository.findByVisitId(visitId)
                .map(mapper::toChargeDTO);
    }

    @Override
    public void setChargeOfVisit(int visitId, int charge) {
        Optional<Charge> optCharge = chargeRepository.findByVisitId(visitId);
        if (optCharge.isPresent()) {
            Charge currentCharge = optCharge.get();
            ChargeDTO prev = mapper.toChargeDTO(currentCharge);
            currentCharge.setCharge(charge);
            currentCharge = chargeRepository.save(currentCharge);
            practiceLogger.logChargeUpdated(prev, mapper.toChargeDTO(currentCharge));
        } else {
            ChargeDTO newCharge = new ChargeDTO();
            newCharge.visitId = visitId;
            newCharge.charge = charge;
            enterCharge(newCharge);
        }
    }

    @Override
    public void enterPayment(PaymentDTO paymentDTO) {
        Payment payment = mapper.fromPaymentDTO(paymentDTO);
        payment = paymentRepository.save(payment);
        practiceLogger.logPaymentCreated(mapper.toPaymentDTO(payment));
    }

    @Override
    public List<PaymentDTO> listPayment(int visitId) {
        return paymentRepository.findByVisitIdOrderByPaytimeDesc(visitId).stream()
                .map(mapper::toPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        Visit visit = visitRepository.findById(visitId);
        return mapper.toVisitDTO(visit);
    }

    @Override
    public int enterVisit(VisitDTO visitDTO) {
        Visit visit = mapper.fromVisitDTO(visitDTO);
        visit = visitRepository.save(visit);
        practiceLogger.logVisitCreated(mapper.toVisitDTO(visit));
        return visit.getVisitId();
    }

    @Override
    public void updateVisit(VisitDTO visitDTO) {
        VisitDTO prev = getVisit(visitDTO.visitId);
        Visit visit = mapper.fromVisitDTO(visitDTO);
        visit = visitRepository.save(visit);
        practiceLogger.logVisitUpdated(prev, mapper.toVisitDTO(visit));
    }

    @Override
    public List<Integer> listVisitIds() {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findAllVisitIds(sort);
    }

    @Override
    public List<Integer> listVisitIdsForPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findVisitIdsByPatient(patientId, sort);
    }

    @Override
    public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtForPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findVisitIdVisitedAtByPatient(patientId, sort).stream()
                .map(result -> {
                    Integer visitId = (Integer) result[0];
                    String visitedAt = (String) result[1];
                    VisitIdVisitedAtDTO visitIdVisitedAtDTO = new VisitIdVisitedAtDTO();
                    visitIdVisitedAtDTO.visitId = visitId;
                    visitIdVisitedAtDTO.visitedAt = visitedAt;
                    return visitIdVisitedAtDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitPatientDTO> listVisitWithPatient(int page, int itemsPerPage) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, sort);
        return visitRepository.findAllWithPatient(pageRequest).stream()
                .map(this::resultToVisitPatientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShinryouFullDTO getShinryouFull(int shinryouId) {
        Object[] result = shinryouRepository.findOneWithMaster(shinryouId).get(0);
        return resultToShinryouFullDTO(result);
    }

    @Override
    public ShinryouDTO getShinryou(int shinryouId) {
        return mapper.toShinryouDTO(shinryouRepository.findById(shinryouId));
    }

    @Override
    public void batchDeleteShinryou(List<Integer> shinryouIds) {
        List<ShinryouDTO> deletedList = shinryouIds.stream()
                .map(this::getShinryou).collect(Collectors.toList());
        if (deletedList.size() > 0) {
            shinryouRepository.batchDelete(shinryouIds);
            deletedList.forEach(practiceLogger::logShinryouDeleted);
        }
    }

    @Override
    public List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds) {
        if (shinryouIds.size() == 0) {
            return Collections.emptyList();
        }
        return shinryouRepository.findFullByIds(shinryouIds).stream()
                .map(this::resultToShinryouFullDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShinryouFullDTO> listShinryouFull(int visitId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "shinryoucode");
        return shinryouRepository.findByVisitIdWithMaster(visitId, sort).stream()
                .map(this::resultToShinryouFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShinryouDTO enterShinryou(ShinryouDTO shinryouDTO) {
        Shinryou shinryou = mapper.fromShinryouDTO(shinryouDTO);
        ShinryouDTO created = mapper.toShinryouDTO(shinryouRepository.save(shinryou));
        practiceLogger.logShinryouCreated(created);
        return created;
    }

    @Override
    public void updateShinryou(ShinryouDTO shinryouDTO) {
        ShinryouDTO prev = getShinryou(shinryouDTO.shinryouId);
        Shinryou updated = shinryouRepository.save(mapper.fromShinryouDTO(shinryouDTO));
        practiceLogger.logShinryouUpdated(prev, mapper.toShinryouDTO(updated));
    }

    @Override
    public void deleteShinryou(int shinryouId) {
        ShinryouDTO deleted = getShinryou(shinryouId);
        shinryouRepository.deleteById(shinryouId);
        practiceLogger.logShinryouDeleted(deleted);
    }

    @Override
    public List<Integer> deleteDuplicateShinryou(int visitId) {
        List<Integer> shinryouIds = new ArrayList<>();
        Set<Integer> shinryoucodes = new HashSet<>();
        shinryouRepository.findByVisitId(visitId).forEach(shinryou -> {
            if (shinryoucodes.contains(shinryou.getShinryoucode())) {
                shinryouIds.add(shinryou.getShinryouId());
            } else {
                shinryoucodes.add(shinryou.getShinryoucode());
            }
        });
        batchDeleteShinryou(shinryouIds);
        return shinryouIds;
    }

    @Override
    public Optional<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
        Date date = Date.valueOf(at);
        return shinryouMasterRepository.findByNameAndDate(name, date).map(mapper::toShinryouMasterDTO);
    }

    @Override
    public Optional<ShinryouMasterDTO> findShinryouMasterByShinryoucode(int shinryoucode, LocalDate at) {
        Date date = Date.valueOf(at);
        return shinryouMasterRepository.findByShinryoucodeAndDate(shinryoucode, date).map(mapper::toShinryouMasterDTO);
    }

    @Override
    public DrugFullDTO getDrugFull(int drugId) {
        Object[] result = drugRepository.findOneWithMaster(drugId).get(0);
        return resultToDrugFullDTO(result);
    }

    private DrugFullDTO findDrugFull(int drugId) {
        List<Object[]> list = drugRepository.findOneWithMaster(drugId);
        if (list.size() == 0) {
            return null;
        } else {
            return resultToDrugFullDTO(list.get(0));
        }
    }

    @Override
    public List<DrugDTO> listDrug(int visitId) {
        return drugRepository.findByVisitId(visitId, Sort.by("drugId"))
                .stream()
                .map(mapper::toDrugDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DrugFullDTO> listDrugFull(int visitId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "drugId");
        return drugRepository.findByVisitIdWithMaster(visitId, sort).stream()
                .map(this::resultToDrugFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int enterDrug(DrugDTO drugDTO) {
        Drug drug = mapper.fromDrugDTO(drugDTO);
        drug.setDrugId(null);
        drug = drugRepository.save(drug);
        practiceLogger.logDrugCreated(mapper.toDrugDTO(drug));
        return drug.getDrugId();
    }

    @Override
    public DrugDTO getDrug(int drugId) {
        return mapper.toDrugDTO(drugRepository.findById(drugId));
    }

    @Override
    public void deleteDrug(int drugId) {
        DrugDTO deleted = getDrug(drugId);
        drugRepository.deleteById(drugId);
        practiceLogger.logDrugDeleted(deleted);
    }

    @Override
    public void updateDrug(DrugDTO drugDTO) {
        DrugDTO prev = getDrug(drugDTO.drugId);
        Drug drug = mapper.fromDrugDTO(drugDTO);
        drug = drugRepository.save(drug);
        practiceLogger.logDrugUpdated(prev, mapper.toDrugDTO(drug));
    }

    @Override
    public void markDrugsAsPrescribedForVisit(int visitId) {
        List<DrugDTO> prevDrugs = listDrug(visitId);
        drugRepository.markAsPrescribedForVisit(visitId);
        List<DrugDTO> updatedDrugs = listDrug(visitId);
        for (int i = 0; i < prevDrugs.size(); i++) {
            DrugDTO prev = prevDrugs.get(i);
            if (prev.prescribed == 0) {
                practiceLogger.logDrugUpdated(prev, updatedDrugs.get(i));
            }
        }
    }

    @Override
    public List<DrugFullDTO> searchPrevDrug(int patientId) {
        List<Integer> drugIds = drugRepository.findNaifukuAndTonpukuPatternByPatient(patientId);
        drugIds.addAll(drugRepository.findGaiyouPatternByPatient(patientId));
        drugIds.sort(Comparator.<Integer>naturalOrder().reversed());
        return drugIds.stream().map(this::findDrugFull).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<DrugFullDTO> searchPrevDrug(int patientId, String text) {
        List<Integer> drugIds = drugRepository.findNaifukuAndTonpukuPatternByPatient(patientId, text);
        drugIds.addAll(drugRepository.findGaiyouPatternByPatient(patientId, text));
        drugIds.sort(Comparator.<Integer>naturalOrder().reversed());
        return drugIds.stream().map(this::findDrugFull).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void batchUpdateDrugDays(List<Integer> drugIds, int days) {
        List<DrugDTO> prevDrugs = drugIds.stream()
                .map(this::getDrug).collect(Collectors.toList());
        drugRepository.batchUpdateDays(drugIds, days);
        List<DrugDTO> updatedDrugs = drugIds.stream()
                .map(this::getDrug).collect(Collectors.toList());
        for (int i = 0; i < prevDrugs.size(); i++) {
            DrugDTO prev = prevDrugs.get(i);
            if (prev.days != days) {
                practiceLogger.logDrugUpdated(prev, updatedDrugs.get(i));
            }
        }
    }

    private List<TextDTO> listText(int visitId) {
        List<Text> texts = textRepository.findByVisitId(visitId);
        return texts.stream().map(mapper::toTextDTO).collect(Collectors.toList());
    }

    @Override
    public TextDTO getText(int textId) {
        Text text = textRepository.findById(textId);
        return mapper.toTextDTO(text);
    }

    @Override
    public int enterText(TextDTO textDTO) {
        Text text = mapper.fromTextDTO(textDTO);
        text.setTextId(null);
        text = textRepository.save(text);
        int textId = text.getTextId();
        textDTO.textId = textId;
        practiceLogger.logTextCreated(textDTO);
        return textId;
    }

    @Override
    public void updateText(TextDTO textDTO) {
        TextDTO prev = getText(textDTO.textId);
        Text text = mapper.fromTextDTO(textDTO);
        textRepository.save(text);
        practiceLogger.logTextUpdated(prev, textDTO);
    }

    @Override
    public void deleteText(int textId) {
        TextDTO textDTO = getText(textId);
        textRepository.deleteById(textId);
        practiceLogger.logTextDeleted(textDTO);
    }

    @Override
    public TextVisitPageDTO searchText(int patientId, String text, int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.Direction.ASC, "textId");
        Page<Object[]> pageResult = textRepository.searchText(patientId, text, pageRequest);
        TextVisitPageDTO result = new TextVisitPageDTO();
        result.totalPages = pageResult.getTotalPages();
        result.page = page;
        result.textVisits = pageResult.getContent().stream()
                .map(this::resultToTextVisitDTO).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ShinryouMasterDTO> searchShinryouMaster(String text, String at) {
        return shinryouMasterRepository.search(text, at, Sort.by("shinryoucode")).stream()
                .map(mapper::toShinryouMasterDTO).collect(Collectors.toList());
    }

    @Override
    public ShinryouMasterDTO getShinryouMaster(int shinryoucode, LocalDate at) {
        Date atDate = Date.valueOf(at);
        return mapper.toShinryouMasterDTO(shinryouMasterRepository.findOneByShinryoucodeAndDate(shinryoucode, atDate));
    }

    @Override
    public VisitFullDTO getVisitFull(int visitId) {
        VisitDTO visitDTO = getVisit(visitId);
        return getVisitFull(visitDTO);
    }

    private VisitFullDTO getVisitFull(Visit visit) {
        return getVisitFull(mapper.toVisitDTO(visit));
    }

    private VisitFullDTO getVisitFull(VisitDTO visitDTO) {
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

    private VisitFull2DTO getVisitFull2(Visit visit) {
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

    @Override
    public VisitFullPageDTO listVisitFull(int patientId, int page) {
        int itemsPerPage = 10;
        Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.Direction.DESC, "visitId");
        Page<Visit> pageVisit = visitRepository.findByPatientId(patientId, pageable);
        VisitFullPageDTO visitFullPageDTO = new VisitFullPageDTO();
        visitFullPageDTO.totalPages = pageVisit.getTotalPages();
        visitFullPageDTO.page = page;
        visitFullPageDTO.visits = pageVisit.getContent().stream().map(this::getVisitFull).collect(Collectors.toList());
        return visitFullPageDTO;
    }

    @Override
    public VisitFull2PageDTO listVisitFull2(int patientId, int page) {
        int itemsPerPage = 10;
        Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.Direction.DESC, "visitId");
        Page<Visit> pageVisit = visitRepository.findByPatientId(patientId, pageable);
        VisitFull2PageDTO visitFull2PageDTO = new VisitFull2PageDTO();
        visitFull2PageDTO.totalPages = pageVisit.getTotalPages();
        visitFull2PageDTO.page = page;
        visitFull2PageDTO.visits = pageVisit.getContent().stream().map(this::getVisitFull2).collect(Collectors.toList());
        return visitFull2PageDTO;
    }

    @Override
    public ConductDTO getConduct(int conductId) {
        Conduct conduct = conductRepository.findById(conductId);
        return mapper.toConductDTO(conduct);
    }

    @Override
    public ConductFullDTO getConductFull(int conductId) {
        ConductDTO conductDTO = getConduct(conductId);
        return extendConduct(conductDTO);
    }

    @Override
    public List<ConductFullDTO> listConductFullByIds(List<Integer> conductIds) {
        if (conductIds.size() == 0) {
            return Collections.emptyList();
        }
        return conductRepository.listConductByIds(conductIds, Sort.by("conductId")).stream()
                .map(mapper::toConductDTO)
                .map(this::extendConduct)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVisitFromReception(int visitId) {
        Optional<Wqueue> wqueueOpt = wqueueRepository.tryFindByVisitId(visitId);
        if (wqueueOpt.isPresent()) {
            Wqueue wqueue = wqueueOpt.get();
            if (wqueue.getWaitState() != WqueueWaitState.WaitExam.getCode()) {
                throw new RuntimeException("診察の状態が診察待ちでないため、削除できません。");
            }
        }
        VisitDTO visitDTO = getVisit(visitId);
        deleteVisitSafely(visitId);
    }

    @Override
    public void deleteVisitSafely(int visitId) {
        VisitFullDTO visit = getVisitFull(visitId);
        if (visit.texts.size() > 0) {
            throw new RuntimeException("文章があるので、診察を削除できません。");
        }
        if (visit.drugs.size() > 0) {
            throw new RuntimeException("投薬があるので、診察を削除できません。");
        }
        if (visit.shinryouList.size() > 0) {
            throw new RuntimeException("診療行為があるので、診察を削除できません。");
        }
        if (visit.conducts.size() > 0) {
            throw new RuntimeException("処置があるので、診察を削除できません。");
        }
        Optional<Charge> optCharge = chargeRepository.findByVisitId(visitId);
        if (optCharge.isPresent()) {
            throw new RuntimeException("請求があるので、診察を削除できません。");
        }
        List<Payment> payments = paymentRepository.findByVisitId(visitId);
        if (payments.size() > 0) {
            throw new RuntimeException("支払い記録があるので、診察を削除できません。");
        }
        Optional<Wqueue> optWqueue = wqueueRepository.tryFindByVisitId(visitId);
        optWqueue.ifPresent(wqueue -> {
            wqueueRepository.delete(wqueue);
            practiceLogger.logWqueueDeleted(mapper.toWqueueDTO(wqueue));
        });
        Optional<PharmaQueue> optPharmaQueue = pharmaQueueRepository.findByVisitId(visitId);
        optPharmaQueue.ifPresent(pharmaQueue -> {
            pharmaQueueRepository.delete(pharmaQueue);
            practiceLogger.logPharmaQueueDeleted(mapper.toPharmaQueueDTO(pharmaQueue));
        });
        visitRepository.deleteById(visitId);
        practiceLogger.logVisitDeleted(visit.visit);
    }

    private ConductFullDTO extendConduct(ConductDTO conductDTO) {
        int conductId = conductDTO.conductId;
        ConductFullDTO conductFullDTO = new ConductFullDTO();
        conductFullDTO.conduct = conductDTO;
        conductFullDTO.gazouLabel = findGazouLabel(conductId);
        conductFullDTO.conductShinryouList = listConductShinryouFull(conductId);
        conductFullDTO.conductDrugs = listConductDrugFull(conductId);
        conductFullDTO.conductKizaiList = listConductKizaiFull(conductId);
        return conductFullDTO;
    }

    @Override
    public GazouLabelDTO findGazouLabel(int conductId) {
        Optional<GazouLabel> gazouLabel = gazouLabelRepository.findOneByConductId(conductId);
        return gazouLabel.map(gazouLabel1 -> mapper.toGazouLabelDTO(gazouLabel1)).orElse(null);
    }

    @Override
    public String findGazouLabelString(int conductId) {
        GazouLabelDTO gazouLabelDTO = findGazouLabel(conductId);
        return gazouLabelDTO == null ? null : gazouLabelDTO.label;
    }

    @Override
    public void enterGazouLabel(GazouLabelDTO gazoulabelDTO) {
        GazouLabel gazouLabel = mapper.fromGazouLabelDTO(gazoulabelDTO);
        gazouLabel = gazouLabelRepository.save(gazouLabel);
        practiceLogger.logGazouLabelCreated(mapper.toGazouLabelDTO(gazouLabel));
    }

    @Override
    public void deleteConduct(int conductId) {
        Optional<GazouLabel> optGazouLabel = gazouLabelRepository.findOneByConductId(conductId);
        optGazouLabel.ifPresent(gazouLabel -> {
            GazouLabelDTO deleted = mapper.toGazouLabelDTO(gazouLabel);
            gazouLabelRepository.delete(gazouLabel);
            practiceLogger.logGazouLabelDeleted(deleted);
        });
        conductShinryouRepository.findByConductId(conductId).forEach(conductShinryou -> {
            ConductShinryouDTO deleted = mapper.toConductShinryouDTO(conductShinryou);
            conductShinryouRepository.delete(conductShinryou);
            practiceLogger.logConductShinryouDeleted(deleted);
        });
        conductDrugRepository.findByConductId(conductId).forEach(conductDrug -> {
            ConductDrugDTO deleted = mapper.toConductDrugDTO(conductDrug);
            conductDrugRepository.delete(conductDrug);
            practiceLogger.logConductDrugDeleted(deleted);
        });
        conductKizaiRepository.findByConductId(conductId).forEach(conductKizai -> {
            ConductKizaiDTO deleted = mapper.toConductKizaiDTO(conductKizai);
            conductKizaiRepository.delete(conductKizai);
            practiceLogger.logConductKizaiDeleted(deleted);
        });
        ConductDTO deletedConduct = mapper.toConductDTO(conductRepository.findById(conductId));
        conductRepository.deleteById(conductId);
        practiceLogger.logConductDeleted(deletedConduct);
    }

    @Override
    public void modifyGazouLabel(int conductId, String label) {
        Optional<GazouLabel> optGazouLabel = gazouLabelRepository.findOneByConductId(conductId);
        if (optGazouLabel.isPresent()) {
            GazouLabel gazouLabel = optGazouLabel.get();
            GazouLabelDTO prev = mapper.toGazouLabelDTO(gazouLabel);
            gazouLabel.setLabel(label);
            gazouLabel = gazouLabelRepository.save(gazouLabel);
            practiceLogger.logGazouLabelUpdated(prev, mapper.toGazouLabelDTO(gazouLabel));
        } else {
            GazouLabelDTO gazouLabel = new GazouLabelDTO();
            gazouLabel.conductId = conductId;
            gazouLabel.label = label;
            enterGazouLabel(gazouLabel);
        }
    }

    @Override
    public void deleteGazouLabel(int conductId){
        gazouLabelRepository.findOneByConductId(conductId)
                .ifPresent(gazouLabel -> gazouLabelRepository.delete(gazouLabel));
    }

    @Override
    public int enterConduct(ConductDTO conductDTO) {
        Conduct conduct = mapper.fromConductDTO(conductDTO);
        conduct = conductRepository.save(conduct);
        practiceLogger.logConductCreated(mapper.toConductDTO(conduct));
        return conduct.getConductId();
    }

    @Override
    public List<ConductDTO> listConducts(int visitId) {
        return conductRepository.findByVisitId(visitId, Sort.by("conductId")).stream()
                .map(mapper::toConductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConductFullDTO> listConductFull(int visitId) {
        return listConducts(visitId).stream()
                .map(this::extendConduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConductShinryouDTO> listConductShinryou(int conductId) {
        return conductShinryouRepository.findByConductId(conductId, Sort.by("conductShinryouId")).stream()
                .map(mapper::toConductShinryouDTO).collect(Collectors.toList());
    }

    private List<ConductShinryouFullDTO> listConductShinryouFull(int conductId) {
        return conductShinryouRepository.findByConductIdWithMaster(conductId).stream()
                .map(this::resultToConductShinryouFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ConductShinryouFullDTO getConductShinryouFull(int conductShinryouId) {
        List<Object[]> results = conductShinryouRepository.findFull(conductShinryouId);
        if (results.size() == 0) {
            throw new RuntimeException("canoot find conduct shinryou: " + conductShinryouId);
        } else if (results.size() != 1) {
            throw new RuntimeException("cannot happen in getConductShinryouFull");
        }
        return resultToConductShinryouFullDTO(results.get(0));
    }

    @Override
    public ConductDrugFullDTO getConductDrugFull(int conductDrugId) {
        List<Object[]> results = conductDrugRepository.findFull(conductDrugId);
        if (results.size() == 0) {
            throw new RuntimeException("canoot find conduct drug: " + conductDrugId);
        } else if (results.size() != 1) {
            throw new RuntimeException("cannot happen in getConductDrugFull");
        }
        return resultToConductDrugFullDTO(results.get(0));
    }

    @Override
    public ConductKizaiFullDTO getConductKizaiFull(int conductKizaiId) {
        List<Object[]> results = conductKizaiRepository.findFull(conductKizaiId);
        if (results.size() == 0) {
            throw new RuntimeException("canoot find conduct kizai: " + conductKizaiId);
        } else if (results.size() != 1) {
            throw new RuntimeException("cannot happen in getConductKizaiFull");
        }
        return resultToConductKizaiFullDTO(results.get(0));
    }

    @Override
    public int enterConductShinryou(ConductShinryouDTO conductShinryouDTO) {
        ConductShinryou conductShinryou = mapper.fromConductShinryouDTO(conductShinryouDTO);
        conductShinryou = conductShinryouRepository.save(conductShinryou);
        practiceLogger.logConductShinryouCreated(mapper.toConductShinryouDTO(conductShinryou));
        return conductShinryou.getConductShinryouId();
    }

    @Override
    public void deleteConductShinryou(int conductShinryouId) {
        ConductShinryouDTO deleted = mapper.toConductShinryouDTO(conductShinryouRepository.findById(conductShinryouId));
        conductShinryouRepository.deleteById(conductShinryouId);
        practiceLogger.logConductShinryouDeleted(deleted);
    }

    @Override
    public List<ConductDrugDTO> listConductDrug(int conductId) {
        return conductDrugRepository.findByConductId(conductId, Sort.by("conductDrugId")).stream()
                .map(mapper::toConductDrugDTO).collect(Collectors.toList());
    }

    private List<ConductDrugFullDTO> listConductDrugFull(int conductId) {
        return conductDrugRepository.findByConductIdWithMaster(conductId).stream()
                .map(this::resultToConductDrugFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int enterConductDrug(ConductDrugDTO conductDrugDTO) {
        ConductDrug conductDrug = mapper.fromConductDrugDTO(conductDrugDTO);
        conductDrug = conductDrugRepository.save(conductDrug);
        practiceLogger.logConductDrugCreated(mapper.toConductDrugDTO(conductDrug));
        return conductDrug.getConductDrugId();
    }

    @Override
    public void deleteConductDrug(int conductDrugId) {
        ConductDrugDTO deleted = mapper.toConductDrugDTO(conductDrugRepository.findById(conductDrugId));
        conductDrugRepository.deleteById(conductDrugId);
        practiceLogger.logConductDrugDeleted(deleted);
    }

    @Override
    public List<ConductKizaiDTO> listConductKizai(int conductId) {
        return conductKizaiRepository.findByConductId(conductId, Sort.by("conductKizaiId")).stream()
                .map(mapper::toConductKizaiDTO).collect(Collectors.toList());
    }

    private List<ConductKizaiFullDTO> listConductKizaiFull(int conductId) {
        return conductKizaiRepository.findByConductIdWithMaster(conductId).stream()
                .map(this::resultToConductKizaiFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int enterConductKizai(ConductKizaiDTO conductKizaiDTO) {
        ConductKizai conductKizai = mapper.fromConductKizaiDTO(conductKizaiDTO);
        conductKizai = conductKizaiRepository.save(conductKizai);
        practiceLogger.logConductKizaiCreated(mapper.toConductKizaiDTO(conductKizai));
        return conductKizai.getConductKizaiId();
    }

    @Override
    public void deleteConductKizai(int conductKizaiId) {
        ConductKizaiDTO deleted = mapper.toConductKizaiDTO(conductKizaiRepository.findById(conductKizaiId));
        conductKizaiRepository.deleteById(conductKizaiId);
        practiceLogger.logConductKizaiDeleted(deleted);
    }

    @Override
    public Optional<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at) {
        Date date = Date.valueOf(at);
        return kizaiMasterRepository.findByNameAndDate(name, date).map(mapper::toKizaiMasterDTO);
    }

    @Override
    public Optional<KizaiMasterDTO> findKizaiMasterByKizaicode(int kizaicode, LocalDate at) {
        Date date = Date.valueOf(at);
        return kizaiMasterRepository.findByKizaicodeAndDate(kizaicode, date).map(mapper::toKizaiMasterDTO);
    }

    @Override
    public List<KizaiMasterDTO> searchKizaiMasterByName(String text, LocalDate at) {
        Date date = Date.valueOf(at);
        return kizaiMasterRepository.searchByName(text, date, Sort.by("yomi")).stream()
                .map(mapper::toKizaiMasterDTO).collect(Collectors.toList());
    }

    @Override
    public void modifyConductKind(int conductId, int kind) {
        Conduct c = conductRepository.findById(conductId);
        ConductDTO prev = mapper.toConductDTO(c);
        c.setKind(kind);
        c = conductRepository.save(c);
        practiceLogger.logConductUpdated(prev, mapper.toConductDTO(c));
    }

    @Override
    public HokenDTO getHokenForVisit(VisitDTO visitDTO) {
        HokenDTO hokenDTO = new HokenDTO();
        if (visitDTO.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = mapper.toShahokokuhoDTO(shahokokuhoRepository.findById(visitDTO.shahokokuhoId));
        }
        if (visitDTO.koukikoureiId > 0) {
            hokenDTO.koukikourei = mapper.toKoukikoureiDTO(koukikoureiRepository.findById(visitDTO.koukikoureiId));
        }
        if (visitDTO.roujinId > 0) {
            hokenDTO.roujin = mapper.toRoujinDTO(roujinRepository.findById(visitDTO.roujinId));
        }
        if (visitDTO.kouhi1Id > 0) {
            hokenDTO.kouhi1 = mapper.toKouhiDTO(kouhiRepository.findById(visitDTO.kouhi1Id));
        }
        if (visitDTO.kouhi2Id > 0) {
            hokenDTO.kouhi2 = mapper.toKouhiDTO(kouhiRepository.findById(visitDTO.kouhi2Id));
        }
        if (visitDTO.kouhi3Id > 0) {
            hokenDTO.kouhi3 = mapper.toKouhiDTO(kouhiRepository.findById(visitDTO.kouhi3Id));
        }
        return hokenDTO;
    }

    @Override
    public List<PaymentVisitPatientDTO> listRecentPayment(int n) {
        PageRequest pageRequest = PageRequest.of(0, n, Sort.Direction.DESC, "visitId");
        List<Integer> visitIds = paymentRepository.findFinalPayment(pageRequest).stream()
                .map(Payment::getVisitId).collect(Collectors.toList());
        if (visitIds.isEmpty()) {
            return Collections.emptyList();
        }
        return paymentRepository.findFullFinalPayment(visitIds, pageRequest).stream()
                .map(this::resultToPaymentVisitPatient).collect(Collectors.toList());
    }

    @Override
    public List<PaymentVisitPatientDTO> listPaymentByPatient(int patientId, int n) {
        PageRequest pageRequest = PageRequest.of(0, n, Sort.Direction.DESC, "visitId");
        return paymentRepository.findFullByPatient(patientId, pageRequest).getContent().stream()
                .map(this::resultToPaymentVisitPatient).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> listFinalPayment(int n) {
        PageRequest pageRequest = PageRequest.of(0, n, Sort.Direction.DESC, "visitId");
        return paymentRepository.findFinalPayment(pageRequest).stream()
                .map(mapper::toPaymentDTO).collect(Collectors.toList());
    }

    @Override
    public void finishCashier(PaymentDTO paymentDTO) {
        Payment payment = mapper.fromPaymentDTO(paymentDTO);
        payment = paymentRepository.save(payment);
        practiceLogger.logPaymentCreated(mapper.toPaymentDTO(payment));
        Optional<PharmaQueue> optPharmaQueue = pharmaQueueRepository.findByVisitId(paymentDTO.visitId);
        Optional<Wqueue> optWqueue = wqueueRepository.tryFindByVisitId(paymentDTO.visitId);
        if (optPharmaQueue.isPresent()) {
            if (optWqueue.isPresent()) {
                changeWqueueState(paymentDTO.visitId, WqueueWaitState.WaitDrug.getCode());
            }
        } else {
            if (optWqueue.isPresent()) {
                Wqueue wqueue = optWqueue.get();
                deleteWqueue(mapper.toWqueueDTO(wqueue));
            }
        }
    }

    @Override
    public Optional<PharmaQueueDTO> findPharmaQueue(int visitId) {
        return pharmaQueueRepository.findByVisitId(visitId).map(mapper::toPharmaQueueDTO);
    }

    @Override
    public List<PharmaQueueFullDTO> listPharmaQueueFullForPrescription() {
        return pharmaQueueRepository.findFull().stream()
                .map(result -> {
                    PharmaQueueFullDTO pharmaQueueFullDTO = new PharmaQueueFullDTO();
                    pharmaQueueFullDTO.pharmaQueue = mapper.toPharmaQueueDTO((PharmaQueue) result[0]);
                    pharmaQueueFullDTO.patient = mapper.toPatientDTO((Patient) result[1]);
                    Optional<Wqueue> optWqueue = wqueueRepository.tryFindByVisitId(pharmaQueueFullDTO.pharmaQueue.visitId);
                    pharmaQueueFullDTO.wqueue = optWqueue.map(mapper::toWqueueDTO).orElse(null);
                    pharmaQueueFullDTO.visitId = pharmaQueueFullDTO.pharmaQueue.visitId;
                    return pharmaQueueFullDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PharmaQueueFullDTO> listPharmaQueueFullForToday() {
        List<Integer> visitIds = visitRepository.findVisitIdForToday(Sort.by("visitId"));
        if (visitIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, WqueueDTO> wqueueMap = new HashMap<>();
        Map<Integer, PharmaQueueDTO> pharmaQueueMap = new HashMap<>();
        wqueueRepository.findAll().forEach(wq -> wqueueMap.put(wq.getVisitId(), mapper.toWqueueDTO(wq)));
        pharmaQueueRepository.findAll().forEach(pq -> pharmaQueueMap.put(pq.getVisitId(), mapper.toPharmaQueueDTO(pq)));
        return visitRepository.findByVisitIdsWithPatient(visitIds).stream()
                .map(result -> {
                    Visit visit = (Visit) result[0];
                    int visitId = visit.getVisitId();
                    Patient patient = (Patient) result[1];
                    PharmaQueueFullDTO pharmaQueueFullDTO = new PharmaQueueFullDTO();
                    pharmaQueueFullDTO.visitId = visitId;
                    pharmaQueueFullDTO.patient = mapper.toPatientDTO(patient);
                    pharmaQueueFullDTO.pharmaQueue = pharmaQueueMap.get(visitId);
                    pharmaQueueFullDTO.wqueue = wqueueMap.get(visitId);
                    return pharmaQueueFullDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PharmaQueueFullDTO getPharmaQueueFull(int visitId) {
        VisitDTO visitDTO = getVisit(visitId);
        PharmaQueueFullDTO result = new PharmaQueueFullDTO();
        result.visitId = visitId;
        result.patient = getPatient(visitDTO.patientId);
        result.pharmaQueue = pharmaQueueRepository.findByVisitId(visitId)
                .map(mapper::toPharmaQueueDTO).orElse(null);
        result.wqueue = wqueueRepository.tryFindByVisitId(visitId)
                .map(mapper::toWqueueDTO).orElse(null);
        return result;
    }

    @Override
    public void deletePharmaQueue(PharmaQueueDTO pharmaQueueDTO) {
        PharmaQueue pharmaQueue = mapper.fromPharmaQueueDTO(pharmaQueueDTO);
        pharmaQueueRepository.delete(pharmaQueue);
        practiceLogger.logPharmaQueueDeleted(pharmaQueueDTO);
    }

    @Override
    public PharmaDrugDTO getPharmaDrugByIyakuhincode(int iyakuhincode) {
        return mapper.toPharmaDrugDTO(pharmaDrugRepository.findByIyakuhincode(iyakuhincode));
    }

    @Override
    public Optional<PharmaDrugDTO> findPharmaDrugByIyakuhincode(int iyakuhincode) {
        return pharmaDrugRepository.tryFindByIyakuhincode(iyakuhincode)
                .map(mapper::toPharmaDrugDTO);
    }

    @Override
    public List<PharmaDrugDTO> collectPharmaDrugByIyakuhincodes(List<Integer> iyakuhincodes) {
        if (iyakuhincodes.size() == 0) {
            return Collections.emptyList();
        } else {
            return pharmaDrugRepository.collectByIyakuhincodes(iyakuhincodes).stream()
                    .map(mapper::toPharmaDrugDTO).collect(Collectors.toList());
        }
    }

    @Override
    public void enterPharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        PharmaDrug pharmaDrug = mapper.fromPharmaDrugDTO(pharmaDrugDTO);
        pharmaDrug = pharmaDrugRepository.save(pharmaDrug);
        practiceLogger.logPharmaDrugCreated(mapper.toPharmaDrugDTO(pharmaDrug));
    }

    @Override
    public void updatePharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        PharmaDrugDTO prev = mapper.toPharmaDrugDTO(pharmaDrugRepository.findById(pharmaDrugDTO.iyakuhincode));
        PharmaDrug pharmaDrug = mapper.fromPharmaDrugDTO(pharmaDrugDTO);
        pharmaDrug = pharmaDrugRepository.save(pharmaDrug);
        practiceLogger.logPharmaDrugUpdated(prev, mapper.toPharmaDrugDTO(pharmaDrug));
    }

    @Override
    public void deletePharmaDrug(int iyakuhincode) {
        PharmaDrugDTO deleted = mapper.toPharmaDrugDTO(pharmaDrugRepository.findById(iyakuhincode));
        pharmaDrugRepository.deleteById(iyakuhincode);
        practiceLogger.logPharmaDrugDeleted(deleted);
    }

    @Override
    public List<PharmaDrugNameDTO> searchPharmaDrugNames(String text) {
        return pharmaDrugRepository.searchNames(text).stream()
                .map(result -> {
                    PharmaDrugNameDTO pharmaDrugNameDTO = new PharmaDrugNameDTO();
                    pharmaDrugNameDTO.iyakuhincode = (Integer) result[0];
                    pharmaDrugNameDTO.name = (String) result[1];
                    pharmaDrugNameDTO.yomi = (String) result[2];
                    return pharmaDrugNameDTO;
                })
                .sorted(Comparator.comparing(a -> a.yomi))
                .collect(Collectors.toList());
    }

    @Override
    public List<PharmaDrugNameDTO> listAllPharmaDrugNames() {
        return pharmaDrugRepository.findAllPharmaDrugNames().stream()
                .map(result -> {
                    PharmaDrugNameDTO pharmaDrugNameDTO = new PharmaDrugNameDTO();
                    pharmaDrugNameDTO.iyakuhincode = (Integer) result[0];
                    pharmaDrugNameDTO.name = (String) result[1];
                    pharmaDrugNameDTO.yomi = (String) result[2];
                    return pharmaDrugNameDTO;
                })
                .sorted(Comparator.comparing(a -> a.yomi))
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitTextDrugDTO> listVisitTextDrug(List<Integer> visitIds) {
        if (visitIds.size() == 0) {
            return Collections.emptyList();
        }
        return visitRepository.findByVisitIds(visitIds, Sort.by(Sort.Direction.DESC, "visitId")).stream()
                .map(visit -> {
                    VisitTextDrugDTO visitTextDrugDTO = new VisitTextDrugDTO();
                    visitTextDrugDTO.visit = mapper.toVisitDTO(visit);
                    visitTextDrugDTO.texts = listText(visit.getVisitId());
                    visitTextDrugDTO.drugs = listDrugFull(visit.getVisitId());
                    return visitTextDrugDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VisitTextDrugPageDTO listVisitTextDrugForPatient(int patientId, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        PageRequest pageRequest = PageRequest.of(page, 10, sort);
        Page<Integer> visitIdPage = visitRepository.pageVisitIdsByPatient(patientId, pageRequest);
        List<Integer> visitIds = visitIdPage.getContent();
        List<VisitTextDrugDTO> visits = visitRepository.findByVisitIds(visitIds, sort)
                .stream()
                .map(visit -> {
                    VisitTextDrugDTO visitTextDrugDTO = new VisitTextDrugDTO();
                    visitTextDrugDTO.visit = mapper.toVisitDTO(visit);
                    visitTextDrugDTO.texts = listText(visit.getVisitId());
                    visitTextDrugDTO.drugs = listDrugFull(visit.getVisitId());
                    return visitTextDrugDTO;
                })
                .collect(Collectors.toList());
        VisitTextDrugPageDTO result = new VisitTextDrugPageDTO();
        result.totalPages = visitIdPage.getTotalPages();
        result.page = page;
        result.visitTextDrugs = visits;
        return result;
    }

    @Override
    public VisitTextDrugPageDTO listVisitTextDrugByPatientAndIyakuhincode(int patientId, int iyakuhincode, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        PageRequest pageRequest = PageRequest.of(page, 10, sort);
        Page<Integer> visitIdPage = drugRepository.pageVisitIdsByPatientAndIyakuhincode(patientId, iyakuhincode, pageRequest);
        List<Integer> visitIds = visitIdPage.getContent();
        List<VisitTextDrugDTO> visits = visitRepository.findByVisitIds(visitIds, sort)
                .stream()
                .map(visit -> {
                    VisitTextDrugDTO visitTextDrugDTO = new VisitTextDrugDTO();
                    visitTextDrugDTO.visit = mapper.toVisitDTO(visit);
                    visitTextDrugDTO.texts = listText(visit.getVisitId());
                    visitTextDrugDTO.drugs = listDrugFull(visit.getVisitId());
                    return visitTextDrugDTO;
                })
                .collect(Collectors.toList());
        VisitTextDrugPageDTO result = new VisitTextDrugPageDTO();
        result.totalPages = visitIdPage.getTotalPages();
        result.page = page;
        result.visitTextDrugs = visits;
        return result;
    }

    private List<Integer> listIyakuhincodeForPatient(int patientId) {
        return drugRepository.findIyakuhincodeByPatient(patientId);
    }

    @Override
    public Optional<String> findNameForIyakuhincode(int iyakuhincode){
        return findNamesForIyakuhincodes(Collections.singletonList(iyakuhincode)).stream()
                .findFirst().map(result -> result.name);
    }

    private List<IyakuhincodeNameDTO> findNamesForIyakuhincodes(List<Integer> iyakuhincodes) {
        if (iyakuhincodes.size() == 0) {
            return Collections.emptyList();
        }
        return iyakuhinMasterRepository.findNameForIyakuhincode(iyakuhincodes, Sort.by(Sort.Direction.ASC, "yomi")).stream()
                .map(result -> {
                    Integer iyakuhincode = (Integer) result[0];
                    String name = (String) result[1];
                    IyakuhincodeNameDTO iyakuhincodeNameDTO = new IyakuhincodeNameDTO();
                    iyakuhincodeNameDTO.iyakuhincode = iyakuhincode;
                    iyakuhincodeNameDTO.name = name;
                    return iyakuhincodeNameDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<IyakuhinMasterDTO> searchIyakuhinByName(String text, LocalDate at) {
        return iyakuhinMasterRepository.searchByName(text, at.toString(), Sort.by("yomi")).stream()
                .map(mapper::toIyakuhinMasterDTO).collect(Collectors.toList());
    }

    @Override
    public List<IyakuhincodeNameDTO> listIyakuhinForPatient(int patientId) {
        List<Integer> iyakuhincodes = listIyakuhincodeForPatient(patientId);
        return findNamesForIyakuhincodes(iyakuhincodes);
    }

    @Override
    public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByIyakuhincodeAndPatientId(int patientId, int iyakuhincode) {
        return drugRepository.findVisitIdVisitedAtByPatientAndIyakuhincode(patientId, iyakuhincode).stream()
                .map(result -> {
                    VisitIdVisitedAtDTO visitIdVisitedAtDTO = new VisitIdVisitedAtDTO();
                    visitIdVisitedAtDTO.visitId = (Integer) result[0];
                    visitIdVisitedAtDTO.visitedAt = (String) result[1];
                    return visitIdVisitedAtDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IyakuhinMasterDTO> findIyakuhinMaster(int iyakuhincode, String at) {
        return iyakuhinMasterRepository.tryFind(iyakuhincode, at).map(mapper::toIyakuhinMasterDTO);
    }

    @Override
    public Optional<IyakuhinMasterDTO> findIyakuhinMasterByIyakuhincode(int iyakuhincode, LocalDate at) {
        Date date = Date.valueOf(at);
        return iyakuhinMasterRepository.findByIyakuhincodeAndDate(iyakuhincode, date).map(mapper::toIyakuhinMasterDTO);
    }

    @Override
    public Integer getLastHotlineId() {
        Optional<Hotline> hotline = hotlineRepository.findTopByOrderByHotlineIdDesc();
        return hotline.map(Hotline::getHotlineId).orElse(0);
    }

    @Override
    public List<HotlineDTO> listHotlineInRange(int lowerHotlineId, int upperHotlineId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "hotlineId");
        return hotlineRepository.findInRange(lowerHotlineId, upperHotlineId, sort).stream()
                .map(mapper::toHotlineDTO).collect(Collectors.toList());
    }

    @Override
    public List<HotlineDTO> listTodaysHotlineInRange(int afterId, int beforeId){
        return hotlineRepository.findTodaysHotlineInRange(afterId, beforeId, Sort.by("hotlineId"))
                .stream()
                .map(mapper::toHotlineDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotlineDTO> listRecentHotline(int thresholdHotlineId) {
        return hotlineRepository.findRecent(thresholdHotlineId).stream()
                .map(mapper::toHotlineDTO).collect(Collectors.toList());
    }

    @Override
    public int enterHotline(HotlineDTO hotlineDTO) {
        Hotline hotline = mapper.fromHotlineDTO(hotlineDTO);
        hotline = hotlineRepository.save(hotline);
        hotlineLogger.logHotlineCreated(mapper.toHotlineDTO(hotline));
        return  hotline.getHotlineId();
    }

    @Override
    public List<HotlineDTO> listTodaysHotline() {
        return hotlineRepository.findTodaysHotline(Sort.by("hotlineId")).stream()
                .map(mapper::toHotlineDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HotlineDTO> getTodaysLastHotline(){
        List<Hotline> result = hotlineRepository.findTodaysHotline(
                PageRequest.of(0, 1, Sort.by("hotlineId").descending()));
        if( result.size() == 0 ){
            return Optional.empty();
        } else {
            return Optional.of(mapper.toHotlineDTO(result.get(0)));
        }
    }

    @Override
    public PrescExampleDTO findPrescExample(int prescExampleId){
        PrescExample example = prescExampleRepository.findById(prescExampleId).orElse(null);
        if( example == null ){
            return null;
        } else {
            return mapper.toPrescExampleDTO(example);
        }
    }

    @Override
    public List<PrescExampleFullDTO> searchPrescExampleFullByName(String text) {
        return prescExampleRepository.searchByNameFull(text).stream()
                .map(this::resultToPrescExampleFullDTO)
//                .map(result -> {
//                    PrescExample prescExample = (PrescExample) result[0];
//                    IyakuhinMaster iyakuhinMaster = (IyakuhinMaster) result[1];
//                    PrescExampleFullDTO prescExampleFullDTO = new PrescExampleFullDTO();
//                    prescExampleFullDTO.prescExample = mapper.toPrescExampleDTO((prescExample));
//                    prescExampleFullDTO.master = mapper.toIyakuhinMasterDTO(iyakuhinMaster);
//                    return prescExampleFullDTO;
//                })
                .collect(Collectors.toList());
    }

    @Override
    public int enterPrescExample(PrescExampleDTO dto){
        PrescExample example = mapper.fromPrescExampleDTO(dto);
        example.setPrescExampleId(null);
        example = prescExampleRepository.save(example);
        return example.getPrescExampleId();
    }

    @Override
    public void updatePrescExample(PrescExampleDTO dto){
        PrescExample example = mapper.fromPrescExampleDTO(dto);
        prescExampleRepository.save(example);
    }

    @Override
    public void deletePrescExample(int prescExampleId){
        PrescExample example = prescExampleRepository.findById(prescExampleId).orElse(null);
        if( example == null ){
            throw new RuntimeException("Cannot find presc example with id " + prescExampleId);
        }
        prescExampleRepository.delete(example);
    }

    @Override
    public List<PrescExampleFullDTO> listAllPrescExample(){
        return prescExampleRepository.findAllFull().stream().map(this::resultToPrescExampleFullDTO)
                .sorted(Comparator.comparing(e -> e.master.yomi))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseFullDTO> listCurrentDiseaseFull(int patientId) {
        return diseaseRepository.findCurrentWithMaster(patientId, Sort.by("diseaseId")).stream()
                .map(this::resultToDiseaseFullDTO)
                .peek(diseaseFullDTO -> diseaseFullDTO.adjList =
                        diseaseAdjRepository.findByDiseaseIdWithMaster(diseaseFullDTO.disease.diseaseId, Sort.by("diseaseAdjId"))
                                .stream()
                                .map(this::resultToDiseaseAdjFullDTO)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseFullDTO> listDiseaseFull(int patientId) {
        return diseaseRepository.findAllWithMaster(patientId, Sort.by("diseaseId")).stream()
                .map(this::resultToDiseaseFullDTO)
                .peek(diseaseFullDTO -> diseaseFullDTO.adjList =
                        diseaseAdjRepository.findByDiseaseIdWithMaster(diseaseFullDTO.disease.diseaseId, Sort.by("diseaseAdjId"))
                                .stream()
                                .map(this::resultToDiseaseAdjFullDTO)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public long countDiseaseByPatient(int patientId) {
        return diseaseRepository.countByPatientId(patientId);
    }

    @Override
    public List<DiseaseFullDTO> pageDiseaseFull(int patientId, int page, int itemsPerPage) {
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, Sort.Direction.DESC, "diseaseId");
        return diseaseRepository.findAllWithMaster(patientId, pageRequest).stream()
                .map(this::resultToDiseaseFullDTO)
                .peek(diseaseFullDTO -> diseaseFullDTO.adjList =
                        diseaseAdjRepository.findByDiseaseIdWithMaster(diseaseFullDTO.disease.diseaseId, Sort.by("diseaseAdjId"))
                                .stream()
                                .map(this::resultToDiseaseAdjFullDTO)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public DiseaseFullDTO getDiseaseFull(int diseaseId) {
        List<Object[]> resultList = diseaseRepository.findFull(diseaseId);
        if (resultList.size() == 0) {
            throw new RuntimeException("Cannot find full disease. " + diseaseId);
        }
        Object[] result = resultList.get(0);
        DiseaseFullDTO diseaseFullDTO = new DiseaseFullDTO();
        diseaseFullDTO.disease = mapper.toDiseaseDTO((Disease) result[0]);
        diseaseFullDTO.master = mapper.toByoumeiMasterDTO(((ByoumeiMaster) result[1]));
        diseaseFullDTO.adjList =
                diseaseAdjRepository.findByDiseaseIdWithMaster(diseaseFullDTO.disease.diseaseId, Sort.by("diseaseAdjId"))
                        .stream()
                        .map(this::resultToDiseaseAdjFullDTO)
                        .collect(Collectors.toList());
        return diseaseFullDTO;
    }

    @Override
    public int enterDisease(DiseaseDTO diseaseDTO, List<DiseaseAdjDTO> adjDTOList) {
        Disease disease = mapper.fromDiseaseDTO(diseaseDTO);
        disease.setDiseaseId(null);
        disease = diseaseRepository.save(disease);
        practiceLogger.logDiseaseCreated(mapper.toDiseaseDTO(disease));
        int diseaseId = disease.getDiseaseId();
        adjDTOList.forEach(adjDTO -> {
            DiseaseAdj adj = mapper.fromDiseaseAdjDTO(adjDTO);
            adj.setDiseaseAdjId(null);
            adj.setDiseaseId(diseaseId);
            adj = diseaseAdjRepository.save(adj);
            practiceLogger.logDiseaseAdjCreated(mapper.toDiseaseAdjDTO(adj));
        });
        return diseaseId;
    }

    @Override
    public void modifyDiseaseEndReason(int diseaseId, LocalDate endDate, char reason) {
        Disease d = diseaseRepository.findById(diseaseId);
        DiseaseDTO prev = mapper.toDiseaseDTO(d);
        d.setEndReason(reason);
        d.setEndDate(endDate.toString());
        d = diseaseRepository.save(d);
        practiceLogger.logDiseaseUpdated(prev, mapper.toDiseaseDTO(d));
    }

    @Override
    public void modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        DiseaseDTO diseaseDTO = diseaseModifyDTO.disease;
        Disease d = diseaseRepository.findById(diseaseDTO.diseaseId);
        DiseaseDTO prevDisease = mapper.toDiseaseDTO(d);
        if (!diseaseDTO.equals(prevDisease)) {
            d.setShoubyoumeicode(diseaseDTO.shoubyoumeicode);
            d.setStartDate(diseaseDTO.startDate);
            d.setEndDate(diseaseDTO.endDate);
            d.setEndReason(diseaseDTO.endReason);
            d = diseaseRepository.save(d);
            practiceLogger.logDiseaseUpdated(prevDisease, mapper.toDiseaseDTO(d));
        }
        List<DiseaseAdj> adjList = diseaseAdjRepository.findByDiseaseId(diseaseDTO.diseaseId, Sort.by("diseaseAdjId"));
        List<Integer> prevAdjCodes = adjList.stream().map(DiseaseAdj::getShuushokugocode).collect(Collectors.toList());
        if (!prevAdjCodes.equals(diseaseModifyDTO.shuushokugocodes)) {
            if (adjList.size() > 0) {
                adjList.forEach(adj -> {
                    DiseaseAdjDTO deleted = mapper.toDiseaseAdjDTO(adj);
                    diseaseAdjRepository.delete(adj);
                    practiceLogger.logDiseaseAdjDeleted(deleted);
                });
            }
            if (diseaseModifyDTO.shuushokugocodes != null) {
                diseaseModifyDTO.shuushokugocodes.forEach(shuushokugocode -> {
                    DiseaseAdj adj = new DiseaseAdj();
                    adj.setDiseaseId(diseaseDTO.diseaseId);
                    adj.setShuushokugocode(shuushokugocode);
                    adj = diseaseAdjRepository.save(adj);
                    practiceLogger.logDiseaseAdjCreated(mapper.toDiseaseAdjDTO(adj));
                });
            }
        }
    }

    @Override
    public void deleteDisease(int diseaseId) {
        List<DiseaseAdj> adjList = diseaseAdjRepository.findByDiseaseId(diseaseId, Sort.by("diseaseAdjId"));
        adjList.forEach(adj -> {
            DiseaseAdjDTO prev = mapper.toDiseaseAdjDTO(adj);
            diseaseAdjRepository.delete(adj);
            practiceLogger.logDiseaseAdjDeleted(prev);
        });
        Disease d = diseaseRepository.findById(diseaseId);
        DiseaseDTO prevDisease = mapper.toDiseaseDTO(d);
        diseaseRepository.delete(d);
        practiceLogger.logDiseaseDeleted(prevDisease);
    }

    @Override
    public List<ByoumeiMasterDTO> searchByoumeiMaster(String text, LocalDate at) {
        Date atDate = Date.valueOf(at);
        return byoumeiMasterRepository.searchByName(text, atDate).stream()
                .map(mapper::toByoumeiMasterDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ByoumeiMasterDTO> findByoumeiMasterByName(String name, LocalDate at) {
        Date atDate = Date.valueOf(at);
        return byoumeiMasterRepository.findByName(name, atDate).map(mapper::toByoumeiMasterDTO);
    }

    @Override
    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(String text) {
        return shuushokugoMasterRepository.searchByName(text).stream()
                .map(mapper::toShuushokugoMasterDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ShuushokugoMasterDTO> findShuushokugoMasterByName(String name) {
        return shuushokugoMasterRepository.findByName(name).map(mapper::toShuushokugoMasterDTO);
    }

    @Override
    public TextVisitPatientPageDTO searchTextGlobally(String text, int page, int itemsPerPage) {
        Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.Direction.DESC, "textId");
        Page<Object[]> result = textRepository.searchTextGlobally(text, pageable);
        TextVisitPatientPageDTO retval = new TextVisitPatientPageDTO();
        retval.page = page;
        retval.totalPages = result.getTotalPages();
        retval.textVisitPatients = result.getContent().stream()
                .map(cols -> {
                    TextVisitPatientDTO value = new TextVisitPatientDTO();
                    value.text = mapper.toTextDTO((Text) cols[0]);
                    value.visit = mapper.toVisitDTO((Visit) cols[1]);
                    value.patient = mapper.toPatientDTO((Patient) cols[2]);
                    return value;
                })
                .collect(Collectors.toList());
        return retval;
    }

    @Override
    public VisitDrugPageDTO pageVisitIdHavingDrug(int patientId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "visitId"));
        Page<Integer> visitIdPage = visitRepository.pageVisitIdHavingDrug(patientId, pageable);
        VisitDrugPageDTO resultPage = new VisitDrugPageDTO();
        resultPage.page = page;
        resultPage.totalPages = visitIdPage.getTotalPages();
        resultPage.visitDrugs = visitIdPage.getContent().stream()
                .map(visitId -> {
                    VisitDrugDTO visitDrug = new VisitDrugDTO();
                    visitDrug.visit = getVisit(visitId);
                    visitDrug.drugs = listDrugFull(visitId);
                    return visitDrug;
                })
                .collect(Collectors.toList());
        return resultPage;
    }

    @Override
    public List<VisitChargePatientDTO> listVisitChargePatientAt(LocalDate at) {
        return visitRepository.listVisitChargePatientAt(at.toString(), Sort.by("visitId"))
                .stream()
                .map(obs -> {
                    VisitDTO visit = mapper.toVisitDTO((Visit) obs[0]);
                    ChargeDTO charge = mapper.toChargeDTO((Charge) obs[1]);
                    PatientDTO patient = mapper.toPatientDTO((Patient) obs[2]);
                    VisitChargePatientDTO dto = new VisitChargePatientDTO();
                    dto.visit = visit;
                    dto.charge = charge;
                    dto.patient = patient;
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds){
        return shoukiRepository.batchGetShouki(visitIds).stream()
                .map(mapper::toShoukiDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ShoukiDTO> findShouki(int visitId){
        return shoukiRepository.findOneByVisitId(visitId).map(mapper::toShoukiDTO);
    }

    @Override
    public void enterShouki(ShoukiDTO shoukiDTO){
        Shouki shouki = mapper.fromShoukiDTO(shoukiDTO);
        shoukiRepository.save(shouki);
    }

    @Override
    public void updateShouki(ShoukiDTO shoukiDTO){
        Shouki shouki = mapper.fromShoukiDTO(shoukiDTO);
        shoukiRepository.save(shouki);
    }

    @Override
    public void deleteShouki(int visitId){
        shoukiRepository.findOneByVisitId(visitId)
                .ifPresent(shouki -> shoukiRepository.delete(shouki));
    }

    private ShinryouFullDTO resultToShinryouFullDTO(Object[] result) {
        Shinryou shinryou = (Shinryou) result[0];
        ShinryouMaster master = (ShinryouMaster) result[1];
        ShinryouFullDTO shinryouFullDTO = new ShinryouFullDTO();
        shinryouFullDTO.shinryou = mapper.toShinryouDTO(shinryou);
        shinryouFullDTO.master = mapper.toShinryouMasterDTO(master);
        return shinryouFullDTO;
    }

    private DrugFullDTO resultToDrugFullDTO(Object[] result) {
        Drug drug = (Drug) result[0];
        IyakuhinMaster master = (IyakuhinMaster) result[1];
        DrugFullDTO drugFullDTO = new DrugFullDTO();
        drugFullDTO.drug = mapper.toDrugDTO(drug);
        drugFullDTO.master = mapper.toIyakuhinMasterDTO(master);
        return drugFullDTO;
    }

    private ConductShinryouFullDTO resultToConductShinryouFullDTO(Object[] result) {
        ConductShinryou conductShinryou = (ConductShinryou) result[0];
        ShinryouMaster master = (ShinryouMaster) result[1];
        ConductShinryouFullDTO conductShinryouFull = new ConductShinryouFullDTO();
        conductShinryouFull.conductShinryou = mapper.toConductShinryouDTO(conductShinryou);
        conductShinryouFull.master = mapper.toShinryouMasterDTO(master);
        return conductShinryouFull;
    }

    private ConductDrugFullDTO resultToConductDrugFullDTO(Object[] result) {
        ConductDrug conductDrug = (ConductDrug) result[0];
        IyakuhinMaster master = (IyakuhinMaster) result[1];
        ConductDrugFullDTO conductDrugFull = new ConductDrugFullDTO();
        conductDrugFull.conductDrug = mapper.toConductDrugDTO(conductDrug);
        conductDrugFull.master = mapper.toIyakuhinMasterDTO(master);
        return conductDrugFull;
    }

    private ConductKizaiFullDTO resultToConductKizaiFullDTO(Object[] result) {
        ConductKizai conductKizai = (ConductKizai) result[0];
        KizaiMaster master = (KizaiMaster) result[1];
        ConductKizaiFullDTO conductKizaiFull = new ConductKizaiFullDTO();
        conductKizaiFull.conductKizai = mapper.toConductKizaiDTO(conductKizai);
        conductKizaiFull.master = mapper.toKizaiMasterDTO(master);
        return conductKizaiFull;
    }

    private VisitPatientDTO resultToVisitPatientDTO(Object[] result) {
        VisitDTO visitDTO = mapper.toVisitDTO((Visit) result[0]);
        PatientDTO patientDTO = mapper.toPatientDTO((Patient) result[1]);
        VisitPatientDTO visitPatientDTO = new VisitPatientDTO();
        visitPatientDTO.visit = visitDTO;
        visitPatientDTO.patient = patientDTO;
        return visitPatientDTO;
    }

    private PaymentVisitPatientDTO resultToPaymentVisitPatient(Object[] result) {
        PaymentDTO paymentDTO = mapper.toPaymentDTO((Payment) result[0]);
        VisitDTO visitDTO = mapper.toVisitDTO((Visit) result[1]);
        PatientDTO patientDTO = mapper.toPatientDTO((Patient) result[2]);
        PaymentVisitPatientDTO paymentVisitPatientDTO = new PaymentVisitPatientDTO();
        paymentVisitPatientDTO.payment = paymentDTO;
        paymentVisitPatientDTO.visit = visitDTO;
        paymentVisitPatientDTO.patient = patientDTO;
        return paymentVisitPatientDTO;
    }

    private WqueueFullDTO resultToWqueueFull(Object[] result) {
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = mapper.toWqueueDTO((Wqueue) result[0]);
        wqueueFullDTO.patient = mapper.toPatientDTO((Patient) result[1]);
        wqueueFullDTO.visit = mapper.toVisitDTO((Visit) result[2]);
        return wqueueFullDTO;
    }

    private DiseaseFullDTO resultToDiseaseFullDTO(Object[] result) {
        DiseaseDTO disease = mapper.toDiseaseDTO(((Disease) result[0]));
        ByoumeiMasterDTO master = mapper.toByoumeiMasterDTO((ByoumeiMaster) result[1]);
        DiseaseFullDTO diseaseFull = new DiseaseFullDTO();
        diseaseFull.disease = disease;
        diseaseFull.master = master;
        diseaseFull.adjList = new ArrayList<>();
        return diseaseFull;
    }

    private DiseaseAdjFullDTO resultToDiseaseAdjFullDTO(Object[] result) {
        DiseaseAdjFullDTO dto = new DiseaseAdjFullDTO();
        dto.diseaseAdj = mapper.toDiseaseAdjDTO((DiseaseAdj) result[0]);
        dto.master = mapper.toShuushokugoMasterDTO((ShuushokugoMaster) result[1]);
        return dto;
    }

    private TextVisitDTO resultToTextVisitDTO(Object[] result) {
        TextVisitDTO dto = new TextVisitDTO();
        dto.text = mapper.toTextDTO((Text) result[0]);
        dto.visit = mapper.toVisitDTO((Visit) result[1]);
        return dto;
    }

    private PrescExampleFullDTO resultToPrescExampleFullDTO(Object[] result){
        PrescExample prescExample = (PrescExample) result[0];
        IyakuhinMaster iyakuhinMaster = (IyakuhinMaster) result[1];
        PrescExampleFullDTO prescExampleFullDTO = new PrescExampleFullDTO();
        prescExampleFullDTO.prescExample = mapper.toPrescExampleDTO((prescExample));
        prescExampleFullDTO.master = mapper.toIyakuhinMasterDTO(iyakuhinMaster);
        return prescExampleFullDTO;
    }

    @Override
    public List<Integer> listVisitingPatientIdHavingHoken(int year, int month) {
        return visitRepository.listVisitingPatientIdHavingHoken(year, month);
    }

    @Override
    public List<VisitFull2DTO> listVisitByPatientHavingHoken(int patientId, int year, int month) {
        return visitRepository.listVisitIdByPatientHavingHoken(patientId, year, month)
                .stream()
                .map(visitId -> {
                    Visit visit = visitRepository.getOne(visitId);
                    return getVisitFull2(visit);
                })
                .filter(v -> v.shinryouList.size() > 0 || v.drugs.size() > 0 ||
                        v.conducts.size() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseFullDTO> listDiseaseByPatientAt(int patientId, int year, int month) {
        LocalDate validFrom = LocalDate.of(year, month, 1);
        LocalDate validUpto = validFrom.plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);
        return diseaseRepository.listDiseaseIdByPatientAt(patientId, validFrom.toString(), validUpto.toString())
                .stream()
                .map(this::getDiseaseFull)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticeLogDTO> listPracticeLogByDate(LocalDate at) {
        return practiceLogRepository.findByDate(at, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticeLogDTO> listRecentPracticeLog(LocalDate at, int lastId) {
        return practiceLogRepository.findRecent(at, lastId, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PracticeLogDTO> listPracticeLogInRange(LocalDate at, int afterId, int beforeId) {
        return practiceLogRepository.findInRange(at, afterId, beforeId, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PracticeLog insertPracticeLog(LocalDateTime createdAt, String kind, String body) {
        PracticeLog data = new PracticeLog();
        data.setCreatedAt(createdAt);
        data.setKind(kind);
        data.setBody(body);
        return practiceLogRepository.save(data);
    }

    @Override
    public PracticeLogDTO findLastPracticeLog() {
        return practiceLogRepository.findFirstByOrderByPracticeLogIdDesc()
                .map(mapper::toPracticeLogDTO)
                .orElse(null);
    }

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds){
        if( shinryouIds.size() == 0 ){
            return Collections.emptyList();
        } else {
            return shinryouAttrRepository.batchGetShinryouAttr(shinryouIds).stream()
                    .map(mapper::toShinryouAttrDTO).collect(Collectors.toList());
        }
    }

    @Override
    public Optional<ShinryouAttrDTO> findShinryouAttr(int shinryouId){
        return shinryouAttrRepository.findOneByShinryouId(shinryouId)
                .map(mapper::toShinryouAttrDTO);
    }

    @Override
    public ShinryouAttrDTO setShinryouTekiyou(int shinryouId, String tekiyou){
        ShinryouAttr attr = shinryouAttrRepository.findOneByShinryouId(shinryouId)
                .orElseGet(() -> new ShinryouAttr(shinryouId, tekiyou));
        attr.setTekiyou(tekiyou);
        attr = shinryouAttrRepository.save(attr);
        return mapper.toShinryouAttrDTO(attr);
    }

    @Override
    public Optional<ShinryouAttrDTO> deleteShinryouTekiyou(int shinryouId){
        return shinryouAttrRepository.findOneByShinryouId(shinryouId)
                .flatMap(shinryouAttr -> {
                    shinryouAttr.setTekiyou(null);
                    if( shinryouAttr.isEmpty() ){
                        shinryouAttrRepository.delete(shinryouAttr);
                        return Optional.empty();
                    } else {
                        shinryouAttrRepository.save(shinryouAttr);
                        return Optional.of(mapper.toShinryouAttrDTO(shinryouAttr));
                    }
                });
    }

    @Override
    public void enterShinryouAttr(ShinryouAttrDTO dto){
        ShinryouAttr attr = mapper.fromShinryouAttrDTO(dto);
        shinryouAttrRepository.save(attr);
    }

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds){
        if( drugIds.size() == 0 ){
            return Collections.emptyList();
        } else {
            return drugAttrRepository.batchGetDrugAttr(drugIds).stream()
                    .map(mapper::toDrugAttrDTO).collect(Collectors.toList());
        }
    }

    @Override
    public Optional<DrugAttrDTO> findDrugAttr(int drugId){
        return drugAttrRepository.findOneByDrugId(drugId)
                .map(mapper::toDrugAttrDTO);
    }

    @Override
    public DrugAttrDTO setDrugTekiyou(int drugId, String tekiyou){
        DrugAttr attr = drugAttrRepository.findOneByDrugId(drugId)
                .orElseGet(() -> new DrugAttr(drugId, tekiyou));
        attr.setTekiyou(tekiyou);
        attr = drugAttrRepository.save(attr);
        return mapper.toDrugAttrDTO(attr);
    }

    @Override
    public Optional<DrugAttrDTO> deleteDrugTekiyou(int drugId){
        return drugAttrRepository.findOneByDrugId(drugId)
                .flatMap(drugAttr -> {
                    drugAttr.setTekiyou(null);
                    if( drugAttr.isEmpty() ){
                        drugAttrRepository.delete(drugAttr);
                        return Optional.empty();
                    } else {
                        drugAttrRepository.save(drugAttr);
                        return Optional.of(mapper.toDrugAttrDTO(drugAttr));
                    }
                });
    }

    @Override
    public void enterDrugAttr(DrugAttrDTO dto){
        DrugAttr attr = mapper.fromDrugAttrDTO(dto);
        drugAttrRepository.save(attr);
    }

}
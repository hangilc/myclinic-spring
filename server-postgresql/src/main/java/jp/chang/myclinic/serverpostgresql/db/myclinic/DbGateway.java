package jp.chang.myclinic.serverpostgresql.db.myclinic;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.serverpostgresql.HotlineLogger;
import jp.chang.myclinic.serverpostgresql.PracticeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DbGateway {

    //private static Logger logger = LoggerFactory.getLogger(DbGateway.class);
    @Autowired
    private IyakuhinMasterRepository iyakuhinMasterRepository;
    @Autowired
    private ShinryouMasterRepository shinryouMasterRepository;
    @Autowired
    private KizaiMasterRepository kizaiMasterRepository;
    @Autowired
    private ByoumeiMasterRepository byoumeiMasterRepository;
    @Autowired
    private ShuushokugoMasterRepository shuushokugoMasterRepository;
    @Autowired
    private PracticeLogRepository practiceLogRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PracticeLogJdbc practiceLogJdbc;
    @Autowired
    private ShahokokuhoRepository shahokokuhoRepository;
    @Autowired
    private RoujinRepository roujinRepository;
    @Autowired
    private KoukikoureiRepository koukikoureiRepository;
    @Autowired
    private KouhiRepository kouhiRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private HotlineRepository hotlineRepository;
    @Autowired
    private WqueueRepository wqueueRepository;

    @Autowired
    private PracticeLogger practiceLogger;
    @Autowired
    private HotlineLogger hotlineLogger;

    private DTOMapper mapper = new DTOMapper();

    public DbGateway() {

    }

    public List<IyakuhinMasterDTO> searchIyakuhinByName(String text, LocalDate at) {
        return iyakuhinMasterRepository.searchByName(text, at, Sort.by("yomi")).stream()
                .map(mapper::toIyakuhinMasterDTO).collect(Collectors.toList());
    }

    public Optional<IyakuhinMasterDTO> findIyakuhinMaster(int iyakuhincode, LocalDate at) {
        return iyakuhinMasterRepository.tryFind(iyakuhincode, at).map(mapper::toIyakuhinMasterDTO);
    }

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

    public ShinryouMasterDTO getShinryouMaster(int shinryoucode, LocalDate at) {
        return mapper.toShinryouMasterDTO(shinryouMasterRepository.findOneByShinryoucodeAndDate(shinryoucode, at));
    }

    public Optional<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
        return shinryouMasterRepository.findByNameAndDate(name, at).map(mapper::toShinryouMasterDTO);
    }

    public List<ShinryouMasterDTO> searchShinryouMaster(String text, LocalDate at) {
        return shinryouMasterRepository.search(text, at, Sort.by("shinryoucode")).stream()
                .map(mapper::toShinryouMasterDTO).collect(Collectors.toList());
    }

    public List<KizaiMasterDTO> searchKizaiMasterByName(String text, LocalDate at) {
        return kizaiMasterRepository.searchByName(text, at, Sort.by("yomi")).stream()
                .map(mapper::toKizaiMasterDTO).collect(Collectors.toList());
    }

    public Optional<KizaiMasterDTO> findKizaiMasterByKizaicode(int kizaicode, LocalDate at) {
        return kizaiMasterRepository.findByKizaicodeAndDate(kizaicode, at).map(mapper::toKizaiMasterDTO);
    }

    public List<PracticeLogDTO> listPracticeLogByDate(LocalDate at) {
        return practiceLogRepository.findByDate(at, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    public List<PracticeLogDTO> listRecentPracticeLog(LocalDate at, int lastId) {
        return practiceLogRepository.findRecent(at, lastId, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    public List<PracticeLogDTO> listPracticeLogInRange(LocalDate at, int afterId, int beforeId) {
        return practiceLogRepository.findInRange(at, afterId, beforeId, Sort.by("practiceLogId"))
                .stream()
                .map(mapper::toPracticeLogDTO)
                .collect(Collectors.toList());
    }

    public PracticeLog insertPracticeLog(LocalDateTime createdAt, String kind, String body) {
        int id = practiceLogJdbc.insert(createdAt, kind, body);
        PracticeLog data = new PracticeLog();
        data.setPracticeLogId(id);
        data.setCreatedAt(createdAt);
        data.setKind(kind);
        data.setBody(body);
        return data;
    }

    public PracticeLogDTO findLastPracticeLog() {
        return practiceLogRepository.findFirstByOrderByPracticeLogIdDesc()
                .map(mapper::toPracticeLogDTO)
                .orElse(null);
    }

    public PatientDTO getPatient(int patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new RuntimeException("患者情報の取得に失敗しました。");
        }
        return mapper.toPatientDTO(patient);
    }

    public Optional<PatientDTO> findPatient(int patientId) {
        return patientRepository.tryFind(patientId).map(mapper::toPatientDTO);
    }

    public List<PatientDTO> searchPatientByLastName(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByLastNameContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatientByFirstName(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByFirstNameContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatientByLastNameYomi(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByLastNameYomiContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatientByFirstNameYomi(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.findByFirstNameYomiContaining(text, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatientByName(String lastName, String firstName) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.searchPatientByName(lastName, firstName, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatientByYomi(String lastNameYomi, String firstNameYomi) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        try (Stream<Patient> stream = patientRepository.searchPatientByYomi(lastNameYomi, firstNameYomi, sort)) {
            return stream.map(mapper::toPatientDTO).collect(Collectors.toList());
        }
    }

    public List<PatientDTO> searchPatient(String text) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        return patientRepository.searchPatient(text, sort).stream()
                .map(mapper::toPatientDTO).collect(Collectors.toList());
    }

    public List<PatientDTO> searchPatient(String textLastName, String textFirstName) {
        Sort sort = Sort.by(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi");
        return patientRepository.searchPatient(textLastName, textFirstName, sort).stream()
                .map(mapper::toPatientDTO).collect(Collectors.toList());
    }

    public List<PatientDTO> listRecentlyRegisteredPatients(int n) {
        PageRequest pageRequest = PageRequest.of(0, n, Sort.Direction.DESC, "patientId");
        return patientRepository.findAll(pageRequest).map(mapper::toPatientDTO).getContent();
    }

    public int enterPatient(PatientDTO patientDTO) {
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        practiceLogger.logPatientCreated(mapper.toPatientDTO(patient));
        return patient.getPatientId();
    }

    public void updatePatient(PatientDTO patientDTO) {
        PatientDTO prev = getPatient(patientDTO.patientId);
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        practiceLogger.logPatientUpdated(prev, mapper.toPatientDTO(patient));
    }

    public int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
        shahokokuho = shahokokuhoRepository.save(shahokokuho);
        practiceLogger.logShahokokuhoCreated(mapper.toShahokokuhoDTO(shahokokuho));
        return shahokokuho.getShahokokuhoId();
    }

    public void updateShahokokuho(ShahokokuhoDTO shahokokuhoDTO){
        ShahokokuhoDTO prev = getShahokokuho(shahokokuhoDTO.shahokokuhoId);
        Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
        shahokokuhoRepository.save(shahokokuho);
        practiceLogger.logShahokokuhoUpdated(prev, shahokokuhoDTO);
    }

    public void deleteShahokokuho(int shahokokuhoId) {
        ShahokokuhoDTO deleted = mapper.toShahokokuhoDTO(shahokokuhoRepository.findById(shahokokuhoId));
        shahokokuhoRepository.deleteById(shahokokuhoId);
        practiceLogger.logShahokokuhoDeleted(deleted);
    }

    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "shahokokuhoId");
        try (Stream<Shahokokuho> stream = shahokokuhoRepository.findAvailable(patientId, at, sort)) {
            return stream.map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
        }
    }

    private List<ShahokokuhoDTO> findShahokokuhoByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "shahokokuhoId");
        return shahokokuhoRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toShahokokuhoDTO).collect(Collectors.toList());
    }

    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return mapper.toShahokokuhoDTO(shahokokuhoRepository.findById(shahokokuhoId));
    }

    public int enterRoujin(RoujinDTO roujinDTO) {
        Roujin roujin = mapper.fromRoujinDTO(roujinDTO);
        roujin = roujinRepository.save(roujin);
        practiceLogger.logRoujinCreated(mapper.toRoujinDTO(roujin));
        return roujin.getRoujinId();
    }

    public void deleteRoujin(int roujinId) {
        RoujinDTO deleted = mapper.toRoujinDTO(roujinRepository.findById(roujinId));
        roujinRepository.deleteById(roujinId);
        practiceLogger.logRoujinDeleted(deleted);
    }

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "roujinId");
        try (Stream<Roujin> stream = roujinRepository.findAvailable(patientId, at, sort)) {
            return stream.map(mapper::toRoujinDTO).collect(Collectors.toList());
        }
    }

    private List<RoujinDTO> findRoujinByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "roujinId");
        return roujinRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toRoujinDTO).collect(Collectors.toList());
    }

    public RoujinDTO getRoujin(int roujinId) {
        return mapper.toRoujinDTO(roujinRepository.findById(roujinId));
    }

    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return mapper.toKoukikoureiDTO(koukikoureiRepository.findById(koukikoureiId));
    }

    public int enterKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
        koukikourei = koukikoureiRepository.save(koukikourei);
        practiceLogger.logKoukikoureiCreated(mapper.toKoukikoureiDTO(koukikourei));
        return koukikourei.getKoukikoureiId();
    }

    public void updateKoukikourei(KoukikoureiDTO koukikoureiDTO){
        KoukikoureiDTO prev = getKoukikourei(koukikoureiDTO.koukikoureiId);
        Koukikourei koukikourei = mapper.fromKoukikoureiDTO(koukikoureiDTO);
        koukikoureiRepository.save(koukikourei);
        practiceLogger.logKoukikoureiUpdated(prev, koukikoureiDTO);
    }

    public void deleteKoukikourei(int koukikoureiId) {
        KoukikoureiDTO deleted = mapper.toKoukikoureiDTO(koukikoureiRepository.findById(koukikoureiId));
        koukikoureiRepository.deleteById(koukikoureiId);
        practiceLogger.logKoukikoureiDeleted(deleted);
    }

    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "koukikoureiId");
        try (Stream<Koukikourei> stream = koukikoureiRepository.findAvailable(patientId, at, sort)) {
            return stream.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
        }
    }

    private List<KoukikoureiDTO> findKoukikoureiByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "koukikoureiId");
        return koukikoureiRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
    }

    public int enterKouhi(KouhiDTO kouhiDTO) {
        Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
        kouhi = kouhiRepository.save(kouhi);
        practiceLogger.logKouhiCreated(mapper.toKouhiDTO(kouhi));
        return kouhi.getKouhiId();
    }

    public void updateKouhi(KouhiDTO kouhiDTO){
        KouhiDTO prev = getKouhi(kouhiDTO.kouhiId);
        Kouhi kouhi = mapper.fromKouhiDTO(kouhiDTO);
        kouhiRepository.save(kouhi);
        practiceLogger.logKouhiUpdated(prev, kouhiDTO);
    }

    public void deleteKouhi(int kouhiId) {
        KouhiDTO deleted = mapper.toKouhiDTO(kouhiRepository.findById(kouhiId));
        kouhiRepository.deleteById(kouhiId);
        practiceLogger.logKouhiDeleted(deleted);
    }

    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.ASC, "kouhiId");
        try (Stream<Kouhi> stream = kouhiRepository.findAvailable(patientId, at, sort)) {
            return stream.map(mapper::toKouhiDTO).collect(Collectors.toList());
        }
    }

    private List<KouhiDTO> findKouhiByPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "kouhiId");
        return kouhiRepository.findByPatientId(patientId, sort).stream()
                .map(mapper::toKouhiDTO).collect(Collectors.toList());
    }

    public KouhiDTO getKouhi(int kouhiId) {
        return mapper.toKouhiDTO(kouhiRepository.findById(kouhiId));
    }

    public HokenListDTO findHokenByPatient(int patientId) {
        HokenListDTO hokenListDTO = new HokenListDTO();
        hokenListDTO.shahokokuhoListDTO = findShahokokuhoByPatient(patientId);
        hokenListDTO.koukikoureiListDTO = findKoukikoureiByPatient(patientId);
        hokenListDTO.roujinListDTO = findRoujinByPatient(patientId);
        hokenListDTO.kouhiListDTO = findKouhiByPatient(patientId);
        return hokenListDTO;
    }

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

    public VisitDTO getVisit(int visitId) {
        Visit visit = visitRepository.findById(visitId);
        return mapper.toVisitDTO(visit);
    }

    public int enterVisit(VisitDTO visitDTO) {
        Visit visit = mapper.fromVisitDTO(visitDTO);
        visit = visitRepository.save(visit);
        practiceLogger.logVisitCreated(mapper.toVisitDTO(visit));
        return visit.getVisitId();
    }

    public void updateVisit(VisitDTO visitDTO) {
        VisitDTO prev = getVisit(visitDTO.visitId);
        Visit visit = mapper.fromVisitDTO(visitDTO);
        visit = visitRepository.save(visit);
        practiceLogger.logVisitUpdated(prev, mapper.toVisitDTO(visit));
    }

    public List<Integer> listVisitIds() {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findAllVisitIds(sort);
    }

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

    public List<Integer> listVisitIdsForPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findVisitIdsByPatient(patientId, sort);
    }

    public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtForPatient(int patientId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        return visitRepository.findVisitIdVisitedAtByPatient(patientId, sort).stream()
                .map(result -> {
                    Integer visitId = (Integer) result[0];
                    LocalDateTime visitedAt = (LocalDateTime) result[1];
                    VisitIdVisitedAtDTO visitIdVisitedAtDTO = new VisitIdVisitedAtDTO();
                    visitIdVisitedAtDTO.visitId = visitId;
                    visitIdVisitedAtDTO.visitedAt = localDateTimeToSqldatetime(visitedAt);
                    return visitIdVisitedAtDTO;
                })
                .collect(Collectors.toList());
    }

    public List<VisitPatientDTO> listVisitWithPatient(int page, int itemsPerPage) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visitId");
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, sort);
        return visitRepository.findAllWithPatient(pageRequest).stream()
                .map(this::resultToVisitPatientDTO)
                .collect(Collectors.toList());
    }

    public List<VisitPatientDTO> listTodaysVisits() {
        List<Integer> visitIds = visitRepository.findVisitIdForToday(Sort.by("visitId"));
        if (visitIds.size() == 0) {
            return Collections.emptyList();
        }
        return visitRepository.findWithPatient(visitIds, Sort.by("visitId")).stream()
                .map(this::resultToVisitPatientDTO).collect(Collectors.toList());
    }

    public Integer getLastHotlineId() {
        Optional<Hotline> hotline = hotlineRepository.findTopByOrderByHotlineIdDesc();
        return hotline.map(Hotline::getHotlineId).orElse(0);
    }

    public List<HotlineDTO> listHotlineInRange(int lowerHotlineId, int upperHotlineId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "hotlineId");
        return hotlineRepository.findInRange(lowerHotlineId, upperHotlineId, sort).stream()
                .map(mapper::toHotlineDTO).collect(Collectors.toList());
    }

    public List<HotlineDTO> listTodaysHotlineInRange(int afterId, int beforeId){
        return hotlineRepository.findTodaysHotlineInRange(afterId, beforeId, Sort.by("hotlineId"))
                .stream()
                .map(mapper::toHotlineDTO)
                .collect(Collectors.toList());
    }

    public List<HotlineDTO> listRecentHotline(int thresholdHotlineId) {
        return hotlineRepository.findRecent(thresholdHotlineId).stream()
                .map(mapper::toHotlineDTO).collect(Collectors.toList());
    }

    public int enterHotline(HotlineDTO hotlineDTO) {
        Hotline hotline = mapper.fromHotlineDTO(hotlineDTO);
        hotline = hotlineRepository.save(hotline);
        hotlineLogger.logHotlineCreated(mapper.toHotlineDTO(hotline));
        return  hotline.getHotlineId();
    }

    public List<HotlineDTO> listTodaysHotline() {
        return hotlineRepository.findTodaysHotline(Sort.by("hotlineId")).stream()
                .map(mapper::toHotlineDTO)
                .collect(Collectors.toList());
    }

    public Optional<HotlineDTO> getTodaysLastHotline(){
        List<Hotline> result = hotlineRepository.findTodaysHotline(
                PageRequest.of(0, 1, Sort.by("hotlineId").descending()));
        if( result.size() == 0 ){
            return Optional.empty();
        } else {
            return Optional.of(mapper.toHotlineDTO(result.get(0)));
        }
    }

    public void enterWqueue(WqueueDTO wqueueDTO) {
        Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
        wqueueRepository.save(wqueue);
        practiceLogger.logWqueueCreated(wqueueDTO);
    }

    public List<ByoumeiMasterDTO> searchByoumeiMaster(String text, LocalDate at) {
        return byoumeiMasterRepository.searchByName(text, at).stream()
                .map(mapper::toByoumeiMasterDTO).collect(Collectors.toList());
    }

    public Optional<ByoumeiMasterDTO> findByoumeiMasterByName(String name, LocalDate at) {
        return byoumeiMasterRepository.findByName(name, at).map(mapper::toByoumeiMasterDTO);
    }

    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(String text) {
        return shuushokugoMasterRepository.searchByName(text).stream()
                .map(mapper::toShuushokugoMasterDTO).collect(Collectors.toList());
    }

    public Optional<ShuushokugoMasterDTO> findShuushokugoMasterByName(String name) {
        return shuushokugoMasterRepository.findByName(name).map(mapper::toShuushokugoMasterDTO);
    }

    public List<WqueueFullDTO> listWqueueFull() {
        try (Stream<Wqueue> stream = wqueueRepository.findAllAsStream()) {
            return stream.map(this::composeWqueueFullDTO).collect(Collectors.toList());
        }
    }

    public WqueueFullDTO getWqueueFull(int visitId) {
        Wqueue wqueue = wqueueRepository.findOneByVisitId(visitId);
        return composeWqueueFullDTO(wqueue);
    }

    public List<WqueueFullDTO> listWqueueFullByStates(Set<WqueueWaitState> states) {
        if (states.size() == 0) {
            return Collections.emptyList();
        }
        Set<Integer> waitSets = states.stream().mapToInt(WqueueWaitState::getCode).boxed().collect(Collectors.toSet());
        return wqueueRepository.findFullByStateSet(waitSets, Sort.by(Sort.Direction.ASC, "visitId")).stream()
                .map(this::resultToWqueueFull).collect(Collectors.toList());
    }

    public List<WqueueDTO> listWqueueByStates(Set<WqueueWaitState> states, Sort sort) {
        Set<Integer> waitSets = states.stream().mapToInt(WqueueWaitState::getCode).boxed().collect(Collectors.toSet());
        return wqueueRepository.findByStateSet(waitSets, sort).stream()
                .map(mapper::toWqueueDTO).collect(Collectors.toList());
    }

    public Optional<WqueueDTO> findWqueue(int visitId) {
        return wqueueRepository.tryFindByVisitId(visitId).map(mapper::toWqueueDTO);
    }

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

    public void startExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.InExam.getCode());
    }

    public void suspendExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.WaitReExam.getCode());
    }



//    public void endExam(int visitId, int charge) {
//        Visit visit = visitRepository.findById(visitId);
//        boolean isToday = isTodaysVisit(visit);
//        setChargeOfVisit(visitId, charge);
//        Wqueue wqueue = wqueueRepository.tryFindByVisitId(visitId).orElse(null);
//        if (wqueue != null && isToday) {
//            changeWqueueState(visitId, WqueueWaitState.WaitCashier.getCode());
//        } else {
//            if(wqueue != null ){ // it not today
//                deleteWqueue(mapper.toWqueueDTO(wqueue));
//            }
//            Wqueue newWqueue = new Wqueue();
//            newWqueue.setVisitId(visitId);
//            newWqueue.setWaitState(WqueueWaitState.WaitCashier.getCode());
//            enterWqueue(mapper.toWqueueDTO(newWqueue));
//        }
//        pharmaQueueRepository.findByVisitId(visitId).ifPresent(pharmaQueue -> {
//            PharmaQueueDTO deleted = mapper.toPharmaQueueDTO(pharmaQueue);
//            pharmaQueueRepository.deleteByVisitId(visitId);
//            practiceLogger.logPharmaQueueDeleted(deleted);
//        });
//        if (isToday) {
//            int unprescribed = drugRepository.countByVisitIdAndPrescribed(visitId, 0);
//            if (unprescribed > 0) {
//                PharmaQueue pharmaQueue = new PharmaQueue();
//                pharmaQueue.setVisitId(visitId);
//                pharmaQueue.setPharmaState(PharmaQueueState.WaitPack.getCode());
//                pharmaQueueRepository.save(pharmaQueue);
//                practiceLogger.logPharmaQueueCreated(mapper.toPharmaQueueDTO(pharmaQueue));
//            }
//        }
//    }



    private static DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    private String localDateTimeToSqldatetime(LocalDateTime dt){
        return dt.format(sqlDateTimeFormatter);
    }

    private VisitPatientDTO resultToVisitPatientDTO(Object[] result) {
        VisitDTO visitDTO = mapper.toVisitDTO((Visit) result[0]);
        PatientDTO patientDTO = mapper.toPatientDTO((Patient) result[1]);
        VisitPatientDTO visitPatientDTO = new VisitPatientDTO();
        visitPatientDTO.visit = visitDTO;
        visitPatientDTO.patient = patientDTO;
        return visitPatientDTO;
    }

    private WqueueFullDTO composeWqueueFullDTO(Wqueue wqueue){
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = mapper.toWqueueDTO(wqueue);
        wqueueFullDTO.visit = getVisit(wqueue.getVisitId());
        wqueueFullDTO.patient = getPatient(wqueueFullDTO.visit.patientId);
        return wqueueFullDTO;
    }

    private WqueueFullDTO resultToWqueueFull(Object[] result) {
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = mapper.toWqueueDTO((Wqueue) result[0]);
        wqueueFullDTO.patient = mapper.toPatientDTO((Patient) result[1]);
        wqueueFullDTO.visit = mapper.toVisitDTO((Visit) result[2]);
        return wqueueFullDTO;
    }



}

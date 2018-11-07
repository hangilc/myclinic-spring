package jp.chang.myclinic.serverpostgresql.db.myclinic;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.serverpostgresql.PracticeLogger;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private PracticeLogRepository practiceLogRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PracticeLogger practiceLogger;
    @Autowired
    private PracticeLogJdbc practiceLogJdbc;
    @Autowired
    private ShahokokuhoRepository shahokokuhoRepository;
    @Autowired
    private RoujinRepository roujinRepository;
    @Autowired
    private KoukikoureiRepository koukikoureiRepository;

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

    private int countUsageOfShahokokuho(int shahokokuhoId){
        throw new NotYetImplementedException();
        //return visitRepository.countByShahokokuhoId(shahokokuhoId);
    }

    public void deleteShahokokuho(int shahokokuhoId) {
        int usage = countUsageOfShahokokuho(shahokokuhoId);
        if (usage != 0) {
            throw new RuntimeException("この社保・国保はすでに使用されているので、削除できません。");
        }
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

    private int countUsageOfRoujin(int roujinId){
        throw new NotYetImplementedException();
        // return visitRepository.countByRoujinId(roujinId)
    }

    public void deleteRoujin(int roujinId) {
        if (countUsageOfRoujin(roujinId) > 0) {
            throw new RuntimeException("この老人保険はすでに使用されているので、削除できません。");
        }
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

    private int countUsageOfKoukikourei(int koukikoureiId){
        throw new NotYetImplementedException();
    }

    public void deleteKoukikourei(int koukikoureiId) {
        if (countUsageOfKoukikourei(koukikoureiId) > 0) {
            throw new RuntimeException("この後期高齢保険はすでに使用されているので、削除できません。");
        }
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


}

package jp.chang.myclinic.serverpostgresql.db.myclinic;

import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private PatientRepository patientRepository;

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

    public int enterPatient(PatientDTO patientDTO) {
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        practiceLogger.logPatientCreated(mapper.toPatientDTO(patient));
        return patient.getPatientId();
    }

}

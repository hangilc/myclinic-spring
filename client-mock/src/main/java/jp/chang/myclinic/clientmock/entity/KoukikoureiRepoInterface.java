package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.List;

public interface KoukikoureiRepoInterface {
    List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at);

}

package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.KouhiDTO;

import java.time.LocalDate;
import java.util.List;

public interface KouhiRepoInterface {
    List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate atDate);

    KouhiDTO getKouhi(int kouhiId);
}

package jp.chang.myclinic.support.houkatsukensa;

import java.time.LocalDate;

public interface HoukatsuKensaService {
    HoukatsuKensa.Revision getRevision(LocalDate at);
}

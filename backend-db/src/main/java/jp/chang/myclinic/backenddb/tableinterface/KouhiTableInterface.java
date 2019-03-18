package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.KouhiDTO;

public interface KouhiTableInterface extends TableInterface<KouhiDTO> {

  String kouhiId();

  String patientId();

  String futansha();

  String jukyuusha();

  String validFrom();

  String validUpto();
}

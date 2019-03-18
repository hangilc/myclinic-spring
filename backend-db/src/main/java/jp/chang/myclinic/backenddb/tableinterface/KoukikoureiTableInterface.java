package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.KoukikoureiDTO;

public interface KoukikoureiTableInterface extends TableInterface<KoukikoureiDTO> {

  String koukikoureiId();

  String patientId();

  String hokenshaBangou();

  String hihokenshaBangou();

  String futanWari();

  String validFrom();

  String validUpto();
}

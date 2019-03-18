package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.RoujinDTO;

public interface RoujinTableInterface extends TableInterface<RoujinDTO> {

  String roujinId();

  String patientId();

  String shichouson();

  String jukyuusha();

  String futanWari();

  String validFrom();

  String validUpto();
}

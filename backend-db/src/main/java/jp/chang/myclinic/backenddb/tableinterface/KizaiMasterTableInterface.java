package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.KizaiMasterDTO;

public interface KizaiMasterTableInterface extends TableInterface<KizaiMasterDTO> {

  String kizaicode();

  String name();

  String yomi();

  String unit();

  String kingaku();

  String validFrom();

  String validUpto();
}

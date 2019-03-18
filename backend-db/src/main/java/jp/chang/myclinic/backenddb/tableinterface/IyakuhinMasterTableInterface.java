package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public interface IyakuhinMasterTableInterface extends TableInterface<IyakuhinMasterDTO> {

  String iyakuhincode();

  String name();

  String yomi();

  String unit();

  String yakka();

  String madoku();

  String kouhatsu();

  String zaikei();

  String validFrom();

  String validUpto();
}

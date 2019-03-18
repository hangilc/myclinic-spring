package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public interface ShinryouMasterTableInterface extends TableInterface<ShinryouMasterDTO> {

  String shinryoucode();

  String name();

  String tensuu();

  String tensuuShikibetsu();

  String shuukeisaki();

  String houkatsukensa();

  String kensaGroup();

  String validFrom();

  String validUpto();
}

package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

public interface PracticeLogTableInterface extends TableInterface<PracticeLogDTO> {

  String serialId();

  String createdAt();

  String kind();

  String body();
}

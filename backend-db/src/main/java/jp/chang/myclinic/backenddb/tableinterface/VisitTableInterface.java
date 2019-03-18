package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.VisitDTO;

public interface VisitTableInterface extends TableInterface<VisitDTO> {

  String visitId();

  String patientId();

  String visitedAt();

  String shahokokuhoId();

  String roujinId();

  String koukikoureiId();

  String kouhi1Id();

  String kouhi2Id();

  String kouhi3Id();
}

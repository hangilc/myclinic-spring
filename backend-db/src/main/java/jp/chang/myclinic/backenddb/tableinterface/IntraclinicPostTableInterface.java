package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.IntraclinicPostDTO;

public interface IntraclinicPostTableInterface extends TableInterface<IntraclinicPostDTO> {

  String id();

  String content();

  String createdAt();
}

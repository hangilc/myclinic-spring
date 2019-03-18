package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.IntraclinicTagPostDTO;

public interface IntraclinicTagPostTableInterface extends TableInterface<IntraclinicTagPostDTO> {

  String tagId();

  String postId();
}

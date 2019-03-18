package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.IntraclinicCommentDTO;

public interface IntraclinicCommentTableInterface extends TableInterface<IntraclinicCommentDTO> {

  String id();

  String name();

  String content();

  String postId();

  String createdAt();
}

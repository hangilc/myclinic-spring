package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.TextDTO;

public interface TextTableInterface extends TableInterface<TextDTO> {

  String textId();

  String visitId();

  String content();
}

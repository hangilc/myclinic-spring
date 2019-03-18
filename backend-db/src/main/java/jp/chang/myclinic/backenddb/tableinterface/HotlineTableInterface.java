package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.HotlineDTO;

public interface HotlineTableInterface extends TableInterface<HotlineDTO> {

  String hotlineId();

  String message();

  String sender();

  String recipient();

  String postedAt();
}

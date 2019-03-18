package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.PaymentDTO;

public interface PaymentTableInterface extends TableInterface<PaymentDTO> {

  String visitId();

  String amount();

  String paytime();
}

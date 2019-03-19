package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.*;
import jp.chang.myclinic.dto.PaymentDTO;

public interface PaymentTableInterface
    extends TableInterface<PaymentDTO>, Query.Projector<PaymentDTO>, SqlTranslator.TableInfo {}

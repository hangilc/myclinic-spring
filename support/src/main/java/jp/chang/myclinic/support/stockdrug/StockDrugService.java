package jp.chang.myclinic.support.stockdrug;

import java.time.LocalDate;

public interface StockDrugService {
    int resolve(int iyakuhincode, LocalDate at);
}

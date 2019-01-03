package jp.chang.myclinic.dbxfer.db;

import java.math.BigDecimal;

public class BigDecimalColumn extends Column {

    public BigDecimalColumn(String name) {
        super(name, BigDecimal.class, BigDecimal.class);
    }

}

package jp.chang.myclinic.rcpt.newcreate.bill;

import java.util.Objects;

class ConductKizai {

    //private static Logger logger = LoggerFactory.getLogger(ConductKizai.class);
    private int kizaicode;
    private double amount;

    public ConductKizai(int kizaicode, double amount) {
        this.kizaicode = kizaicode;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConductKizai that = (ConductKizai) o;
        return kizaicode == that.kizaicode &&
                Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(kizaicode, amount);
    }
}

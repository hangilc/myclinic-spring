package jp.chang.myclinic.kouhatsukasan;

class Drug {

    String name;
    String yakkacode;
    double amount;

    static Drug copy(Drug src){
        Drug dst = new Drug();
        dst.name = src.name;
        dst.yakkacode = src.yakkacode;
        dst.amount = src.amount;
        return dst;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "name='" + name + '\'' +
                ", yakkacode='" + yakkacode + '\'' +
                ", amount=" + amount +
                '}';
    }
}

package jp.chang.myclinic.master.add;

public class KizaiMasterCSV {

    public String kubun;
    public String masterShubetsu;
    public int kizaicode;
    public String name;
    public String yomi;
    public String unit;
    public String kingakuShubetsu;
    public String kingaku;

    public KizaiMasterCSV(CSVRow row) {
        kubun = row.getString(1);
        masterShubetsu = row.getString(2);
        kizaicode = row.getInt(3);
        name = row.getString(5);
        yomi = row.getString(7);
        unit = row.getString(10);
        kingakuShubetsu = row.getString(11);
        kingaku = row.getString(12);
    }

}

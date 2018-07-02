package jp.chang.myclinic.mastermanip.lib;

public class ShinryouMasterCSV {
    public int kubun;
    public String masterShubetsu;
    public String nyuuGaiTekiyou;
    public String byouShinKubun;
    public String tensuuShikibetsu;
    public int shinryoucode;
    public String name;
    public String tensuu;
    public String shuukeisaki;
    public String houkatsukensa;
    public String oushinKubun;
    public String kensaGroup;
    //public String roujinTekiyou;
//    public String codeShou;
//    public String codeBu;
//    public String codeAlpha;
//    public String codeKubun;

    public ShinryouMasterCSV(){

    }

    public ShinryouMasterCSV(CSVRow row) {
        kubun = row.getInt(1);
        masterShubetsu = row.getString(2);
        nyuuGaiTekiyou = row.getString(13);
        byouShinKubun = row.getString(19);
        tensuuShikibetsu = row.getString(11);
        shinryoucode = row.getInt(3);
        name = row.getString(5);
        tensuu = row.getString(12);
        shuukeisaki = row.getString(15);
        houkatsukensa = twoChars(row.getString(16));
        oushinKubun = row.getString(17);
        kensaGroup = twoChars(row.getString(51));
        //roujinTekiyou = row.getString(14);
//        codeShou = row.getString(90);
//        codeBu = row.getString(91);
//        codeAlpha = row.getString(85);
//        codeKubun = row.getString(92);
    }

    private String twoChars(String s){
        if( s.length() == 1 ){
            return "0" + s;
        } else {
            return s;
        }
    }
}
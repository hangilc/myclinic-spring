package jp.chang.myclinic;

public class ShinryouRow {

    public String kubun;
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
    public String roujinTekiyou;
    public String codeShou;
    public String codeBu;
    public String codeAlpha;
    public String codeKubun;

    public ShinryouRow(Row row) {
        kubun = row.getString(1);
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
        roujinTekiyou = row.getString(14);
        codeShou = row.getString(90);
        codeBu = row.getString(91);
        codeAlpha = row.getString(85);
        codeKubun = row.getString(92);
    }

    private String twoChars(String s){
    	if( s.length() == 1 ){
    		return "0" + s;
    	} else {
    		return s;
    	}
    }

}

    /*
	    henkou_kubun = get_csv_value(sa, 1);
	    if( *henkou_kubun == '1'  // 抹消 
            ||
		    *henkou_kubun == '9'  // 廃止  
        )
		    return 1;
	    master_shubetsu = get_csv_value(sa, 2);
	    if( *master_shubetsu != 'S' ){
		    MessageBox(NULL, "診療行為マスターのデータではありません", "エラー", MB_ICONERROR);
		    return 0;
	    }
	    nyuugaitekiyou = get_csv_value(sa, 13);
	    if( *nyuugaitekiyou == '1' ) return 1;
	    byoushinkubun = get_csv_value(sa, 19);
	    if( *byoushinkubun == '1' ) return 1;
	    tensuu_shikibetsu = get_csv_value(sa, 11);
	    if( *tensuu_shikibetsu == '0' ) return 1;
	    shinryoucode = get_csv_value(sa, 3);
	    name = get_csv_value(sa, 5);
	    tensuu = get_csv_value(sa, 12);
	    shuukeisaki = get_csv_value(sa, 15);
	    houkatsukensa = get_csv_value(sa, 16);
	    if( strlen(houkatsukensa) == 1 ){
		    buf_houkatsukensa[1] = *houkatsukensa;
		    houkatsukensa = buf_houkatsukensa;
	    }
	    oushinkubun = get_csv_value(sa, 17);
	    kensagroup = get_csv_value(sa, 51);
	    if( strlen(kensagroup) == 1 ){
		    buf_kensagroup[1] = *kensagroup;
		    kensagroup = buf_kensagroup;
	    }
	    roujintekiyou = get_csv_value(sa, 14);
	    code_shou = get_csv_value(sa, 90);
	    code_bu   = get_csv_value(sa, 91);
	    code_alpha = get_csv_value(sa, 85);
	    code_kubun = get_csv_value(sa, 92); 
    */

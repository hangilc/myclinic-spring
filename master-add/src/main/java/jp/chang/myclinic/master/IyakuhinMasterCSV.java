package jp.chang.myclinic.master;

public class IyakuhinMasterCSV {
    public String kubun;
    public String masterShubetsu;
    public int iyakuhincode;
    public String name;
    public String yomi;
    public String unit;
    public int kingakuShubetsu;
    public String yakka;
    public int madoku;
    public int kouhatsu;
    public int zaikei;
    public String yakkacode;

    public IyakuhinMasterCSV(CSVRow row){
        kubun = row.getString(1);
        masterShubetsu = row.getString(2);
        iyakuhincode = row.getInt(3);
        name = row.getString(5);
        yomi = row.getString(7);
        unit = row.getString(10);
        kingakuShubetsu = row.getInt(11);
        yakka = row.getString(12);
        madoku = row.getInt(14);
        kouhatsu = row.getInt(17);
        zaikei = row.getInt(28);
        yakkacode = row.getString(32);
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
	    if( *master_shubetsu != 'Y' ){
		    MessageBox(NULL, "医薬品マスターのデータではありません", "エラー", MB_ICONERROR);
		    return 0;
	    }
	    iyakuhincode = get_csv_value(sa, 3);
	    name = get_csv_value(sa, 5);
	    yomi = get_csv_value(sa, 7);
	    unit = get_csv_value(sa, 10);
	    kingaku_shubetsu = get_csv_value(sa, 11);
	    if( *kingaku_shubetsu != '1' )
		    return 1;
	    yakka = get_csv_value(sa, 12);
	    madoku = get_csv_value(sa, 14);
		    // madoku: 麻薬・毒薬など
		    //   0:右記以外、1:麻薬、2:毒薬、3:覚せい剤原料、5:向精神薬
	    kouhatsu = get_csv_value(sa, 17);
		    // kouhatsu: 後発品
		    //   0:右記以外、1:後発品
	    zaikei = get_csv_value(sa, 28);
		    // zaikei: 剤型
		    //   1:内服薬、3:その他、4:注射薬, 6:外用薬、8:歯科用薬剤、9:歯科特定薬剤
	    yakkacode = get_csv_value(sa, 32);
    */

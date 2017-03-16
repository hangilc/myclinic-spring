package jp.chang.myclinic;

public class KizaiRow {

    public String kubun;
    public String masterShubetsu;
    public int kizaicode;
    public String name;
    public String yomi;
    public String unit;
    public String kingakuShubetsu;
    public String kingaku;

    public KizaiRow(Row row) {
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

    /*  
        henkou_kubun = get_csv_value(sa, 1);
        if( *henkou_kubun == '1'  // 抹消 
            ||
            *henkou_kubun == '9'  // 廃止 
        )
            return 1;
        master_shubetsu = get_csv_value(sa, 2);
        if( *master_shubetsu != 'T' ){
            MessageBox(NULL, "特定器材マスターのデータではありません", "エラー", MB_ICONERROR);
            return 0;
        }
        kizaicode = get_csv_value(sa, 3);
        name = get_csv_value(sa, 5);
        yomi = get_csv_value(sa, 7);
        unit = get_csv_value(sa, 10);
        kingaku_shubetsu = get_csv_value(sa, 11);
        if( *kingaku_shubetsu != '1' )
            return 1;
        kingaku = get_csv_value(sa, 12);
    */


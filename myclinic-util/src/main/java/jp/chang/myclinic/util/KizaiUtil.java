package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.ConductKizaiFullDTO;

import java.text.NumberFormat;

public class KizaiUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String kizaiRep(ConductKizaiFullDTO kizaiFull){
        return String.format("%s %s%s", kizaiFull.master.name, numberFormat.format(kizaiFull.conductKizai.amount),
                kizaiFull.master.unit);
    }
}

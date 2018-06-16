package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;

import java.text.NumberFormat;

public class KizaiUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String kizaiRep(ConductKizaiFullDTO kizaiFull){
        return kizaiRep(kizaiFull.conductKizai, kizaiFull.master);
    }

    public static String kizaiRep(ConductKizaiDTO dto, KizaiMasterDTO master){
        return String.format("%s %s%s", master.name, numberFormat.format(dto.amount), master.unit);
    }
}

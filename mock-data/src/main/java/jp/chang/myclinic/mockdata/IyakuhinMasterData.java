package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IyakuhinMasterData {

    public static IyakuhinMasterDTO calonal;

    static {
        IyakuhinMasterDTO master = new IyakuhinMasterDTO();
        master.iyakuhincode = 620000033;
        master.name = "カロナール錠３００　３００ｍｇ";
        master.yomi = "ｶﾛﾅｰﾙｼﾞｮｳ300";
        master.yakka = 7.900000;
        master.unit = "錠";
        master.kouhatsu = '1';
        master.madoku = '0';
        master.zaikei = '1';
        master.validFrom = "2018-04-01";
        master.validUpto = "0000-00-00";
        calonal = master;
    }

    public static IyakuhinMasterDTO loxonin;

    static {
        IyakuhinMasterDTO master = new IyakuhinMasterDTO();
        master.iyakuhincode = 620098801;
        master.name = "ロキソニン錠６０ｍｇ";
        master.yomi = "ﾛｷｿﾆﾝｼﾞｮｳ60MG";
        master.yakka = 14.500000;
        master.unit = "錠";
        master.kouhatsu = '0';
        master.madoku = '0';
        master.zaikei = '1';
        master.validFrom = "2018-04-01";
        master.validUpto = "0000-00-00";
        loxonin = master;
    }

    public static IyakuhinMasterDTO loxoninPap;

    static {
        IyakuhinMasterDTO master = new IyakuhinMasterDTO();
        master.iyakuhincode = 620003477;
        master.name = "ロキソニンパップ１００ｍｇ　１０ｃｍ×１４ｃｍ";
        master.yomi = "ﾛｷｿﾆﾝﾊﾟｯﾌﾟ100MG";
        master.yakka = 34.600000;
        master.unit = "枚";
        master.kouhatsu = '0';
        master.madoku = '0';
        master.zaikei = '6';
        master.validFrom = "2018-04-01";
        master.validUpto = "0000-00-00";
        loxoninPap = master;
    }

}

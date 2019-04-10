package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SampleData {

    public static ClinicInfoDTO sampleClinicInfo = new ClinicInfoDTO();

    static {
        ClinicInfoDTO info = sampleClinicInfo;
        info.name = "テストクリニック";
        info.postalCode = "123-4567";
        info.address = "某県某所１丁目２－３４";
        info.tel = "03-1234-8888";
        info.fax = "03-1234-7777";
        info.todoufukencode = "13";
        info.tensuuhyoucode = "1";
        info.kikancode = "0000000";
        info.homepage = "http://example.com";
        info.doctorName = "試験 データ";
    }

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

    public static PrescExampleDTO calonalPrescExample = new PrescExampleDTO();
    static {
        calonalPrescExample.prescExampleId = 1;
        calonalPrescExample.iyakuhincode = calonal.iyakuhincode;
        calonalPrescExample.masterValidFrom = calonal.validFrom;
        calonalPrescExample.category = DrugCategory.Naifuku.getCode();
        calonalPrescExample.amount = "3";
        calonalPrescExample.usage = "分３　毎食後";
        calonalPrescExample.days = 5;
        calonalPrescExample.comment = "";
    }

    public static PrescExampleFullDTO calonalPrescExampleFull = new PrescExampleFullDTO();
    static {
        calonalPrescExampleFull.master = calonal;
        calonalPrescExampleFull.prescExample = calonalPrescExample;
    }

    public static PrescExampleDTO loxoninTonpukuPrescExample = new PrescExampleDTO();
    static {
        loxoninTonpukuPrescExample.prescExampleId = 2;
        loxoninTonpukuPrescExample.iyakuhincode = loxonin.iyakuhincode;
        loxoninTonpukuPrescExample.masterValidFrom = loxonin.validFrom;
        loxoninTonpukuPrescExample.category = DrugCategory.Tonpuku.getCode();
        loxoninTonpukuPrescExample.amount = "1";
        loxoninTonpukuPrescExample.usage = "疼痛時";
        loxoninTonpukuPrescExample.days = 10;
        loxoninTonpukuPrescExample.comment = "１日３回まで";
    }

    public static PrescExampleFullDTO loxoninTonpukuPrescExampleFull = new PrescExampleFullDTO();
    static {
        loxoninTonpukuPrescExampleFull.master = loxonin;
        loxoninTonpukuPrescExampleFull.prescExample = loxoninTonpukuPrescExample;
    }

    public static PrescExampleDTO loxoninGaiyouPrescExample = new PrescExampleDTO();
    static {
        loxoninGaiyouPrescExample.prescExampleId = 3;
        loxoninGaiyouPrescExample.iyakuhincode = loxoninPap.iyakuhincode;
        loxoninGaiyouPrescExample.masterValidFrom = loxoninPap.validFrom;
        loxoninGaiyouPrescExample.category = DrugCategory.Gaiyou.getCode();
        loxoninGaiyouPrescExample.amount = "28";
        loxoninGaiyouPrescExample.usage = "１日１回、患部に貼付";
        loxoninGaiyouPrescExample.days = 1;
        loxoninGaiyouPrescExample.comment = "１回１枚";
    }

    public static PrescExampleFullDTO loxoninGaiyouPrescExampleFull = new PrescExampleFullDTO();
    static {
        loxoninGaiyouPrescExampleFull.master = loxoninPap;
        loxoninGaiyouPrescExampleFull.prescExample = loxoninGaiyouPrescExample;
    }


}

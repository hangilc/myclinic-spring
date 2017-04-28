package jp.chang.myclinic.consts;


public class MyclinicConsts {

	public static int WqueueStateWaitExam = 0;
	public static int WqueueStateInExam = 1;
	public static int WqueueStateWaitCashier = 2;
	public static int WqueueStateWaitDrug = 3;
	public static int WqueueStateWaitReExam = 4;
	public static int WqueueStateWaitAppoint = 5;
    
	public static int PharmaQueueStateWaitPack = 0;
	public static int PharmaQueueStateInPack   = 1;
	public static int PharmaQueueStatePackDone = 2;

	public static String DiseaseEndReasonNotEnded = "N";
	public static String DiseaseEndReasonCured = "C";
	public static String DiseaseEndReasonStopped = "S";
	public static String DiseaseEndReasonDead = "D";

	public static int DrugCategoryNaifuku = 0;
	public static int DrugCategoryTonpuku = 1;
	public static int DrugCategoryGaiyou  = 2;

	public static int ConductKindHikaChuusha = 0;
	public static int ConductKindJoumyakuChuusha = 1;
	public static int ConductKindOtherChuusha = 2;
	public static int ConductKindGazou = 3;

	public static int ZaikeiNaifuku = 1;
	public static int ZaikeiOther = 3;
	public static int ZaikeiChuusha = 4;
	public static int ZaikeiGaiyou = 6;
	public static int ZaikeiShikaYakuzai = 8;
	public static int ZaikeiShikaTokutei = 9;

	public static int SmallestPostfixShuushokugoCode = 8000;
	public static int LargestPostfixShuushookugoCode = 8999;

	public static String[] MeisaiSections = new String[]{
	        "初・再診料", "医学管理等", "在宅医療", "検査", "画像診断",
	        "投薬", "注射", "処置", "その他"       
	    };

	public static String SHUUKEI_SHOSHIN = "110";
	public static String SHUUKEI_SAISHIN_SAISHIN = "120";
	public static String SHUUKEI_SAISHIN_GAIRAIKANRI = "122";
	public static String SHUUKEI_SAISHIN_JIKANGAI = "123";
	public static String SHUUKEI_SAISHIN_KYUUJITSU = "124";
	public static String SHUUKEI_SAISHIN_SHINYA = "125";
	public static String SHUUKEI_SHIDO = "130";
	public static String SHUUKEI_ZAITAKU = "140";
	public static String SHUUKEI_TOYAKU_NAIFUKUTONPUKUCHOZAI = "210";
	public static String SHUUKEI_TOYAKU_GAIYOCHOZAI = "230";
	public static String SHUUKEI_TOYAKU_SHOHO = "250";
	public static String SHUUKEI_TOYAKU_MADOKU = "260";
	public static String SHUUKEI_TOYAKU_CHOKI = "270";
	public static String SHUUKEI_CHUSHA_SEIBUTSUETC = "300";
	public static String SHUUKEI_CHUSHA_HIKA = "311";
	public static String SHUUKEI_CHUSHA_JOMYAKU = "321";
	public static String SHUUKEI_CHUSHA_OTHERS = "331";
	public static String SHUUKEI_SHOCHI = "400";
	public static String SHUUKEI_SHUJUTSU_SHUJUTSU = "500";
	public static String SHUUKEI_SHUJUTSU_YUKETSU = "502";
	public static String SHUUKEI_MASUI = "540";
	public static String SHUUKEI_KENSA = "600";
	public static String SHUUKEI_GAZOSHINDAN = "700";
	public static String SHUUKEI_OTHERS = "800";

	public static String HOUKATSU_NONE = "00";
	public static String HOUKATSU_KETSUEKIKAGAKU = "01";
	public static String HOUKATSU_ENDOCRINE = "02";
	public static String HOUKATSU_HEPATITIS = "03";
	public static String HOUKATSU_TUMOR = "04";
	public static String HOUKATSU_TUMORMISC = "05";
	public static String HOUKATSU_COAGULO = "06";
	public static String HOUKATSU_AUTOANTIBODY = "07";
	public static String HOUKATSU_TOLERANCE = "08";

}

/*
"use strict";

exports.WqueueStateWaitExam = 0;
exports.WqueueStateInExam = 1;
exports.WqueueStateWaitCashier = 2;
exports.WqueueStateWaitDrug = 3;
exports.WqueueStateWaitReExam = 4;
exports.WqueueStateWaitAppoint = 5;

exports.PharmaQueueStateWaitPack = 0;
exports.PharmaQueueStateInPack   = 1;
exports.PharmaQueueStatePackDone = 2;

exports.DiseaseEndReasonNotEnded = "N";
exports.DiseaseEndReasonCured = "C";
exports.DiseaseEndReasonStopped = "S";
exports.DiseaseEndReasonDead = "D";

exports.DrugCategoryNaifuku = 0;
exports.DrugCategoryTonpuku = 1;
exports.DrugCategoryGaiyou  = 2;

exports.ConductKindHikaChuusha = 0;
exports.ConductKindJoumyakuChuusha = 1;
exports.ConductKindOtherChuusha = 2;
exports.ConductKindGazou = 3;

exports.ZaikeiNaifuku = 1;
exports.ZaikeiOther = 3;
exports.ZaikeiChuusha = 4;
exports.ZaikeiGaiyou = 6;
exports.ZaikeiShikaYakuzai = 8;
exports.ZaikeiShikaTokutei = 9;

exports.SmallestPostfixShuushokugoCode = 8000;
exports.LargestPostfixShuushookugoCode = 8999;

exports.MeisaiSections = [
        "初・再診料", "医学管理等", "在宅医療", "検査", "画像診断",
        "投薬", "注射", "処置", "その他"       
    ];

exports.SHUUKEI_SHOSHIN = "110";
exports.SHUUKEI_SAISHIN_SAISHIN = "120";
exports.SHUUKEI_SAISHIN_GAIRAIKANRI = "122";
exports.SHUUKEI_SAISHIN_JIKANGAI = "123";
exports.SHUUKEI_SAISHIN_KYUUJITSU = "124";
exports.SHUUKEI_SAISHIN_SHINYA = "125";
exports.SHUUKEI_SHIDO = "130";
exports.SHUUKEI_ZAITAKU = "140";
exports.SHUUKEI_TOYAKU_NAIFUKUTONPUKUCHOZAI = "210";
exports.SHUUKEI_TOYAKU_GAIYOCHOZAI = "230";
exports.SHUUKEI_TOYAKU_SHOHO = "250";
exports.SHUUKEI_TOYAKU_MADOKU = "260";
exports.SHUUKEI_TOYAKU_CHOKI = "270";
exports.SHUUKEI_CHUSHA_SEIBUTSUETC = "300";
exports.SHUUKEI_CHUSHA_HIKA = "311";
exports.SHUUKEI_CHUSHA_JOMYAKU = "321";
exports.SHUUKEI_CHUSHA_OTHERS = "331";
exports.SHUUKEI_SHOCHI = "400";
exports.SHUUKEI_SHUJUTSU_SHUJUTSU = "500";
exports.SHUUKEI_SHUJUTSU_YUKETSU = "502";
exports.SHUUKEI_MASUI = "540";
exports.SHUUKEI_KENSA = "600";
exports.SHUUKEI_GAZOSHINDAN = "700";
exports.SHUUKEI_OTHERS = "800";

exports.HOUKATSU_NONE = '00';
exports.HOUKATSU_KETSUEKIKageKU = "01";
exports.HOUKATSU_ENDOCRINE = "02";
exports.HOUKATSU_HEPATITIS = "03";
exports.HOUKATSU_TUMOR = "04";
exports.HOUKATSU_TUMORMISC = "05";
exports.HOUKATSU_COAGULO = "06";
exports.HOUKATSU_AUTOANTIBODY = "07";
exports.HOUKATSU_TOLERANCE = "08";
*/
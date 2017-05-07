package jp.chang.myclinic.consts;


public class MyclinicConsts {

	public static final int WqueueStateWaitExam = 0;
	public static final int WqueueStateInExam = 1;
	public static final int WqueueStateWaitCashier = 2;
	public static final int WqueueStateWaitDrug = 3;
	public static final int WqueueStateWaitReExam = 4;
	public static final int WqueueStateWaitAppoint = 5;
    
	public static final int PharmaQueueStateWaitPack = 0;
	public static final int PharmaQueueStateInPack   = 1;
	public static final int PharmaQueueStatePackDone = 2;

	public static final String DiseaseEndReasonNotEnded = "N";
	public static final String DiseaseEndReasonCured = "C";
	public static final String DiseaseEndReasonStopped = "S";
	public static final String DiseaseEndReasonDead = "D";

	public static final int DrugCategoryNaifuku = 0;
	public static final int DrugCategoryTonpuku = 1;
	public static final int DrugCategoryGaiyou  = 2;

	public static final int ConductKindHikaChuusha = 0;
	public static final int ConductKindJoumyakuChuusha = 1;
	public static final int ConductKindOtherChuusha = 2;
	public static final int ConductKindGazou = 3;

	public static final int ZaikeiNaifuku = 1;
	public static final int ZaikeiOther = 3;
	public static final int ZaikeiChuusha = 4;
	public static final int ZaikeiGaiyou = 6;
	public static final int ZaikeiShikaYakuzai = 8;
	public static final int ZaikeiShikaTokutei = 9;

	public static final int SmallestPostfixShuushokugoCode = 8000;
	public static final int LargestPostfixShuushookugoCode = 8999;

	public static final String[] MeisaiSections = new String[]{
        "初・再診料", "医学管理等", "在宅医療", "検査", "画像診断",
        "投薬", "注射", "処置", "その他"       
    };

	public static final String SHUUKEI_SHOSHIN = "110";
	public static final String SHUUKEI_SAISHIN_SAISHIN = "120";
	public static final String SHUUKEI_SAISHIN_GAIRAIKANRI = "122";
	public static final String SHUUKEI_SAISHIN_JIKANGAI = "123";
	public static final String SHUUKEI_SAISHIN_KYUUJITSU = "124";
	public static final String SHUUKEI_SAISHIN_SHINYA = "125";
	public static final String SHUUKEI_SHIDO = "130";
	public static final String SHUUKEI_ZAITAKU = "140";
	public static final String SHUUKEI_TOYAKU_NAIFUKUTONPUKUCHOZAI = "210";
	public static final String SHUUKEI_TOYAKU_GAIYOCHOZAI = "230";
	public static final String SHUUKEI_TOYAKU_SHOHO = "250";
	public static final String SHUUKEI_TOYAKU_MADOKU = "260";
	public static final String SHUUKEI_TOYAKU_CHOKI = "270";
	public static final String SHUUKEI_CHUSHA_SEIBUTSUETC = "300";
	public static final String SHUUKEI_CHUSHA_HIKA = "311";
	public static final String SHUUKEI_CHUSHA_JOMYAKU = "321";
	public static final String SHUUKEI_CHUSHA_OTHERS = "331";
	public static final String SHUUKEI_SHOCHI = "400";
	public static final String SHUUKEI_SHUJUTSU_SHUJUTSU = "500";
	public static final String SHUUKEI_SHUJUTSU_YUKETSU = "502";
	public static final String SHUUKEI_MASUI = "540";
	public static final String SHUUKEI_KENSA = "600";
	public static final String SHUUKEI_GAZOSHINDAN = "700";
	public static final String SHUUKEI_OTHERS = "800";

	public static final String HOUKATSU_NONE = "00";
	public static final String HOUKATSU_KETSUEKIKAGAKU = "01";
	public static final String HOUKATSU_ENDOCRINE = "02";
	public static final String HOUKATSU_HEPATITIS = "03";
	public static final String HOUKATSU_TUMOR = "04";
	public static final String HOUKATSU_TUMORMISC = "05";
	public static final String HOUKATSU_COAGULO = "06";
	public static final String HOUKATSU_AUTOANTIBODY = "07";
	public static final String HOUKATSU_TOLERANCE = "08";

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
package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DrugEnterInputStateTest {

    private IyakuhinMasterDTO sampleNaifukuMaster = new IyakuhinMasterDTO();

    {
        sampleNaifukuMaster.iyakuhincode = 620000033;
        sampleNaifukuMaster.name = "カロナール錠３００　３００ｍｇ";
        sampleNaifukuMaster.yomi = "ｶﾛﾅｰﾙｼﾞｮｳ300";
        sampleNaifukuMaster.unit = "錠";
        sampleNaifukuMaster.yakka = 7.90;
        sampleNaifukuMaster.madoku = '0';
        sampleNaifukuMaster.kouhatsu = '1';
        sampleNaifukuMaster.zaikei = '1';
        sampleNaifukuMaster.validFrom = "2018-04-01";
        sampleNaifukuMaster.validUpto = "0000-00-00";
    }

    private IyakuhinMasterDTO sampleTonpukuMaster = new IyakuhinMasterDTO();

    {
        sampleTonpukuMaster.iyakuhincode = 620004587;
        sampleTonpukuMaster.name = "ペロリック錠１０ｍｇ";
        sampleTonpukuMaster.yomi = "ﾍﾟﾛﾘｯｸｼﾞｮｳ10MG";
        sampleTonpukuMaster.unit = "錠";
        sampleTonpukuMaster.yakka = 5.60;
        sampleTonpukuMaster.madoku = '0';
        sampleTonpukuMaster.kouhatsu = '1';
        sampleTonpukuMaster.zaikei = '1';
        sampleTonpukuMaster.validFrom = "2018-04-01";
        sampleTonpukuMaster.validUpto = "0000-00-00";
    }

    private IyakuhinMasterDTO sampleGaiyouMaster = new IyakuhinMasterDTO();

    {
        sampleGaiyouMaster.iyakuhincode = 620003477;
        sampleGaiyouMaster.name = "ロキソニンパップ１００ｍｇ　１０ｃｍ×１４ｃｍ";
        sampleGaiyouMaster.yomi = "ﾛｷｿﾆﾝﾊﾟｯﾌﾟ100MG";
        sampleGaiyouMaster.unit = "枚";
        sampleGaiyouMaster.yakka = 34.60;
        sampleGaiyouMaster.madoku = '0';
        sampleGaiyouMaster.kouhatsu = '0';
        sampleGaiyouMaster.zaikei = '6';
        sampleGaiyouMaster.validFrom = "2018-04-01";
        sampleGaiyouMaster.validUpto = "0000-00-00";
    }

    @Test
    public void testSetMasterNaifuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        IyakuhinMasterDTO m = sampleNaifukuMaster;
        state.setMaster(m);
        assertTrue(state.getIyakuhincode() == m.iyakuhincode &&
            state.getDrugName().equals(sampleNaifukuMaster.name) &&
                state.getAmountLabel().equals("用量：") &&
                state.getAmount().equals("") &&
                state.getAmountUnit().equals("錠") &&
                state.getUsage().equals("") &&
                state.getDaysLabel().equals("日数：") &&
                state.getDays().equals("") &&
                state.getDaysUnit().equals("日分") &&
                state.getCategory() == DrugCategory.Naifuku &&
                state.getComment().equals("") &&
                !state.isCommentVisible() &&
                state.getTekiyou().equals("") &&
                !state.isTekiyouVisible() &&
                state.isDaysFixed() &&
                !state.isDaysFixedDisabled()
        );
    }


}

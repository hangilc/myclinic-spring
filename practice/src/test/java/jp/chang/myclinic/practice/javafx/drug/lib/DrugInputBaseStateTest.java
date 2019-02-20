package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DrugInputBaseStateTest {

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
    public void testAdaptToCategoryNaifuku() {
        DrugInputBaseState state = new DrugInputBaseState();
        state.setAmountLabel("一回：");
        state.setDaysLabel("回数：");
        state.setDaysUnit("回分");
        state.setDaysVisible(false);
        state.setCategory(DrugCategory.Naifuku);
        state.adaptToCategory();
        assertTrue(
                state.getAmountLabel().equals("用量：") &&
                        state.getDaysLabel().equals("日数：") &&
                        state.getDaysUnit().equals("日分") &&
                        state.isDaysVisible()
        );
    }

    @Test
    public void testAdaptToCategoryTonpuku() {
        DrugInputBaseState state = new DrugInputBaseState();
        state.setAmountLabel("用量：");
        state.setDaysLabel("日数：");
        state.setDaysUnit("日分");
        state.setDaysVisible(false);
        state.setCategory(DrugCategory.Tonpuku);
        state.adaptToCategory();
        assertTrue(
                state.getAmountLabel().equals("一回：") &&
                        state.getDaysLabel().equals("回数：") &&
                        state.getDaysUnit().equals("回分") &&
                        state.isDaysVisible()
        );
    }

    @Test
    public void testAdaptToCategoryGaiyou() {
        DrugInputBaseState state = new DrugInputBaseState();
        state.setAmountLabel("一回：");
        state.setDaysLabel("回数：");
        state.setDaysUnit("回分");
        state.setDaysVisible(true);
        state.setCategory(DrugCategory.Gaiyou);
        state.adaptToCategory();
        assertTrue(
                state.getAmountLabel().equals("用量：") &&
                        !state.isDaysVisible()
        );
    }

    @Test
    public void testSetMasterNaifuku() {
        DrugInputBaseState state = new DrugInputBaseState();
        IyakuhinMasterDTO m = sampleNaifukuMaster;
        state.setMaster(m);
        assertTrue(
                state.getIyakuhincode() == m.iyakuhincode &&
                        state.getDrugName().equals(m.name) &&
                        state.getAmount().equals("") &&
                        state.getAmountUnit().equals(m.unit) &&
                        state.getUsage().equals("") &&
                        state.getDays().equals("") &&
                        state.getCategory() == DrugCategory.Naifuku &&
                        state.getAmountLabel().equals("用量：") &&
                        state.getDaysLabel().equals("日数：") &&
                        state.getDaysUnit().equals("日分") &&
                        state.isDaysVisible()
        );
    }

    @Test
    public void testSetMasterTonpuku() { // Unless zaikei is gaiyou, category is set to naifuku
        DrugInputBaseState state = new DrugInputBaseState();
        IyakuhinMasterDTO m = sampleTonpukuMaster;
        state.setMaster(m);
        assertTrue(
                state.getIyakuhincode() == m.iyakuhincode &&
                        state.getDrugName().equals(m.name) &&
                        state.getAmount().equals("") &&
                        state.getAmountUnit().equals(m.unit) &&
                        state.getUsage().equals("") &&
                        state.getDays().equals("") &&
                        state.getCategory() == DrugCategory.Naifuku &&
                        state.getAmountLabel().equals("用量：") &&
                        state.getDaysLabel().equals("日数：") &&
                        state.getDaysUnit().equals("日分") &&
                        state.isDaysVisible()
        );
    }

    @Test
    public void testSetMasterGaiyou() {
        DrugInputBaseState state = new DrugInputBaseState();
        IyakuhinMasterDTO m = sampleGaiyouMaster;
        state.setMaster(m);
        assertEquals(state.getIyakuhincode(), m.iyakuhincode);
        assertEquals(state.getDrugName(), m.name);
        assertEquals(state.getAmount(), "");
        assertEquals(state.getAmountUnit(), m.unit);
        assertEquals(state.getUsage(), "");
        assertEquals(state.getDays(), "");
        assertEquals(state.getCategory(), DrugCategory.Gaiyou);
        assertEquals(state.getAmountLabel(), "用量：");
        assertFalse(state.isDaysVisible());
    }

    @Test
    public void testDaysPreservedAfterCategoryAdapt(){
        DrugInputBaseState state = new DrugInputBaseState();
        IyakuhinMasterDTO m = sampleNaifukuMaster;
        state.setMaster(m);
        String days = state.getDays();
        state.setCategory(DrugCategory.Gaiyou);
        state.adaptToCategory();
        assertEquals(days, state.getDays());
    }

}

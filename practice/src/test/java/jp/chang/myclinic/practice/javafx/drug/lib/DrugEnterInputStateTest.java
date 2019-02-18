package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import org.junit.Test;

import static org.junit.Assert.*;

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

    private PrescExampleFullDTO exampleNaifuku = new PrescExampleFullDTO();

    {
        exampleNaifuku.master = sampleNaifukuMaster;
        PrescExampleDTO e = new PrescExampleDTO();
        e.amount = "3";
        e.category = DrugCategory.Naifuku.getCode();
        e.days = 5;
        e.iyakuhincode = exampleNaifuku.master.iyakuhincode;
        e.masterValidFrom = exampleNaifuku.master.validFrom;
        e.prescExampleId = 1;
        e.usage = "分３　毎食後";
        e.comment = "";
        exampleNaifuku.prescExample = e;
    }

    private PrescExampleFullDTO exampleTonpuku = new PrescExampleFullDTO();

    {
        exampleTonpuku.master = sampleTonpukuMaster;
        PrescExampleDTO e = new PrescExampleDTO();
        e.amount = "10";
        e.category = DrugCategory.Tonpuku.getCode();
        e.days = 10;
        e.iyakuhincode = exampleTonpuku.master.iyakuhincode;
        e.masterValidFrom = exampleTonpuku.master.validFrom;
        e.prescExampleId = 2;
        e.usage = "１回１錠　嘔気時（１日３回まで）";
        e.comment = "";
        exampleTonpuku.prescExample = e;
    }

    @Test
    public void testSetMasterNaifuku() {
        DrugEnterInputState state = new DrugEnterInputState();
        IyakuhinMasterDTO m = sampleNaifukuMaster;
        state.setMaster(m);
        assertTrue(state.getIyakuhincode() == m.iyakuhincode &&
                state.getDrugName().equals(m.name) &&
                state.getAmountLabel().equals("用量：") &&
                state.getAmount().equals("") &&
                state.getAmountUnit().equals(m.unit) &&
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
                !state.isDaysFixedDisabled() &&
                state.getDaysBackup().equals("")
        );
    }

    @Test
    public void testSetMasterGaiyou() {
        DrugEnterInputState state = new DrugEnterInputState();
        IyakuhinMasterDTO m = sampleGaiyouMaster;
        state.setMaster(m);
        assertEquals(state.getIyakuhincode(), m.iyakuhincode);
        assertEquals(state.getDrugName(), m.name);
        assertEquals(state.getAmountLabel(), "用量：");
        assertEquals(state.getAmount(), "");
        assertEquals(state.getAmountUnit(), m.unit);
        assertEquals(state.getUsage(), "");
        assertEquals(state.getDays(), "1");
        assertEquals(state.getCategory(), DrugCategory.Gaiyou);
        assertEquals(state.getComment(), "");
        assertFalse(state.isCommentVisible());
        assertEquals(state.getTekiyou(), "");
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals(state.getDaysBackup(), "");
    }

    @Test
    public void testSetPrescExampleNaifuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        PrescExampleFullDTO full = exampleNaifuku;
        state.setPrescExample(full);
        IyakuhinMasterDTO m = full.master;
        PrescExampleDTO e = full.prescExample;
        assertEquals(state.getIyakuhincode(), m.iyakuhincode);
        assertEquals(state.getDrugName(), m.name);
        assertEquals(state.getAmountLabel(), "用量：");
        assertEquals(state.getAmount(), e.amount);
        assertEquals(state.getAmountUnit(), m.unit);
        assertEquals(state.getUsage(), e.usage);
        assertEquals(state.getDaysLabel(), "日数：");
        assertEquals(state.getDays(), "" + e.days);
        assertEquals(state.getDaysUnit(), "日分");
        assertEquals(state.getCategory(), DrugCategory.fromCode(e.category));
        assertEquals(state.getComment(), "");
        assertFalse(state.isCommentVisible());
        assertEquals(state.getTekiyou(), "");
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertFalse(state.isDaysFixedDisabled());
        assertEquals(state.getDaysBackup(), "");
    }

    @Test
    public void testSetPrescExampleTonpuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        PrescExampleFullDTO full = exampleTonpuku;
        state.setPrescExample(full);
        IyakuhinMasterDTO m = full.master;
        PrescExampleDTO e = full.prescExample;
        assertEquals(state.getIyakuhincode(), m.iyakuhincode);
        assertEquals(state.getDrugName(), m.name);
        assertEquals(state.getAmountLabel(), "一回：");
        assertEquals(state.getAmount(), e.amount);
        assertEquals(state.getAmountUnit(), m.unit);
        assertEquals(state.getUsage(), e.usage);
        assertEquals(state.getDaysLabel(), "回数：");
        assertEquals(state.getDays(), "" + e.days);
        assertEquals(state.getDaysUnit(), "回分");
        assertEquals(state.getCategory(), DrugCategory.fromCode(e.category));
        assertEquals(state.getComment(), "");
        assertFalse(state.isCommentVisible());
        assertEquals(state.getTekiyou(), "");
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals(state.getDaysBackup(), "");
    }

}
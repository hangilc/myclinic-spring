package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
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
        e.amount = "1";
        e.category = DrugCategory.Tonpuku.getCode();
        e.days = 10;
        e.iyakuhincode = exampleTonpuku.master.iyakuhincode;
        e.masterValidFrom = exampleTonpuku.master.validFrom;
        e.prescExampleId = 2;
        e.usage = "嘔気時（１日３回まで）";
        e.comment = "";
        exampleTonpuku.prescExample = e;
    }

    private PrescExampleFullDTO exampleGaiyou = new PrescExampleFullDTO();

    {
        exampleGaiyou.master = sampleTonpukuMaster;
        PrescExampleDTO e = new PrescExampleDTO();
        e.amount = "28";
        e.category = DrugCategory.Gaiyou.getCode();
        e.days = 1;
        e.iyakuhincode = exampleGaiyou.master.iyakuhincode;
        e.masterValidFrom = exampleGaiyou.master.validFrom;
        e.prescExampleId = 3;
        e.usage = "１回１枚、１日１回、患部に貼付";
        e.comment = "";
        exampleGaiyou.prescExample = e;
    }

    private DrugFullDTO drugNaifuku = new DrugFullDTO();

    {
        IyakuhinMasterDTO m = sampleNaifukuMaster;
        DrugDTO d = new DrugDTO();
        drugNaifuku.master = m;
        drugNaifuku.drug = d;
        d.drugId = 0;
        d.visitId = 1;
        d.amount = 3;
        d.category = DrugCategory.Naifuku.getCode();
        d.days = 7;
        d.iyakuhincode = m.iyakuhincode;
        d.usage = "分３　毎食後";
        d.prescribed = 0;
    }

    private DrugFullDTO drugTonpuku = new DrugFullDTO();

    {
        IyakuhinMasterDTO m = sampleTonpukuMaster;
        DrugDTO d = new DrugDTO();
        drugTonpuku.master = m;
        drugTonpuku.drug = d;
        d.drugId = 0;
        d.visitId = 2;
        d.amount = 1;
        d.category = DrugCategory.Tonpuku.getCode();
        d.days = 10;
        d.iyakuhincode = m.iyakuhincode;
        d.usage = "嘔気時（１日３回まで）";
        d.prescribed = 0;
    }

    private DrugFullDTO drugGaiyou = new DrugFullDTO();

    {
        IyakuhinMasterDTO m = sampleGaiyouMaster;
        DrugDTO d = new DrugDTO();
        drugGaiyou.master = m;
        drugGaiyou.drug = d;
        d.drugId = 0;
        d.visitId = 3;
        d.amount = 28;
        d.category = DrugCategory.Gaiyou.getCode();
        d.days = 1;
        d.iyakuhincode = m.iyakuhincode;
        d.usage = "嘔気時（１日３回まで）";
        d.prescribed = 0;
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
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("用量：", state.getAmountLabel());
        assertEquals("", state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals("", state.getUsage());
        assertEquals("1", state.getDays());
        assertEquals(DrugCategory.Gaiyou, state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetPrescExampleNaifuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        PrescExampleFullDTO full = exampleNaifuku;
        state.setPrescExample(full);
        IyakuhinMasterDTO m = full.master;
        PrescExampleDTO e = full.prescExample;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("用量：", state.getAmountLabel());
        assertEquals(e.amount, state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(e.usage, state.getUsage());
        assertEquals("日数：", state.getDaysLabel());
        assertEquals("" + e.days, state.getDays());
        assertEquals("日分", state.getDaysUnit());
        assertEquals(DrugCategory.fromCode(e.category), state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertFalse(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetPrescExampleTonpuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        PrescExampleFullDTO full = exampleTonpuku;
        state.setPrescExample(full);
        IyakuhinMasterDTO m = full.master;
        PrescExampleDTO e = full.prescExample;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("一回：", state.getAmountLabel());
        assertEquals(e.amount, state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(e.usage, state.getUsage());
        assertEquals("回数：", state.getDaysLabel());
        assertEquals("" + e.days, state.getDays());
        assertEquals("回分", state.getDaysUnit());
        assertEquals(DrugCategory.fromCode(e.category), state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetPrescExampleGaiyou(){
        DrugEnterInputState state = new DrugEnterInputState();
        PrescExampleFullDTO full = exampleGaiyou;
        state.setPrescExample(full);
        IyakuhinMasterDTO m = full.master;
        PrescExampleDTO e = full.prescExample;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("用量：", state.getAmountLabel());
        assertEquals(e.amount, state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(e.usage, state.getUsage());
        assertEquals("1", state.getDays());
        assertEquals(DrugCategory.fromCode(e.category), state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetDrugNaifuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        DrugFullDTO full = drugNaifuku;
        state.setDrug(full);
        IyakuhinMasterDTO m = full.master;
        DrugDTO d = full.drug;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("用量：", state.getAmountLabel());
        assertEquals("3", state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(d.usage, state.getUsage());
        assertEquals("日数：", state.getDaysLabel());
        assertEquals("" + d.days, state.getDays());
        assertEquals("日分", state.getDaysUnit());
        assertEquals(DrugCategory.fromCode(d.category), state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertFalse(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetDrugTonpuku(){
        DrugEnterInputState state = new DrugEnterInputState();
        DrugFullDTO full = drugTonpuku;
        state.setDrug(full);
        IyakuhinMasterDTO m = full.master;
        DrugDTO d = full.drug;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("一回：", state.getAmountLabel());
        assertEquals("1", state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(d.usage, state.getUsage());
        assertEquals("回数：", state.getDaysLabel());
        assertEquals("" + d.days, state.getDays());
        assertEquals("回分", state.getDaysUnit());
        assertEquals(DrugCategory.Tonpuku, state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

    @Test
    public void testSetDrugGaiyou(){
        DrugEnterInputState state = new DrugEnterInputState();
        DrugFullDTO full = drugGaiyou;
        state.setDrug(full);
        IyakuhinMasterDTO m = full.master;
        DrugDTO d = full.drug;
        assertEquals(m.iyakuhincode, state.getIyakuhincode());
        assertEquals(m.name, state.getDrugName());
        assertEquals("用量：", state.getAmountLabel());
        assertEquals("28", state.getAmount());
        assertEquals(m.unit, state.getAmountUnit());
        assertEquals(d.usage, state.getUsage());
        assertEquals("1", state.getDays());
        assertEquals(DrugCategory.Gaiyou, state.getCategory());
        assertEquals("", state.getComment());
        assertFalse(state.isCommentVisible());
        assertEquals("", state.getTekiyou());
        assertFalse(state.isTekiyouVisible());
        assertTrue(state.isDaysFixed());
        assertTrue(state.isDaysFixedDisabled());
        assertEquals("", state.getDaysBackup());
    }

}

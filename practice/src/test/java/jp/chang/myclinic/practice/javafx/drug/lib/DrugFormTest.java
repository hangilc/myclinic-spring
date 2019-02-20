package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DrugFormTest {

    private DrugInputBaseState sampleBaseState = new DrugInputBaseState();

    {
        sampleBaseState.setIyakuhincode(620000033);
        sampleBaseState.setDrugName("カロナール錠３００　３００ｍｇ");
        sampleBaseState.setAmount("3");
        sampleBaseState.setAmountUnit("錠");
        sampleBaseState.setUsage("分３　毎食後");
        sampleBaseState.setDays("5");
        sampleBaseState.setCategory(DrugCategory.Naifuku);
        sampleBaseState.adaptToCategory();
    }

    @Test
    public void testDrugInputStateCreation(){
        DrugInputState state = new DrugInputState(sampleBaseState);
        boolean ok = sampleBaseState.equals(state);
        assertTrue(ok);
    }

}

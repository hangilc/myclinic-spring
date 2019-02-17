package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class DrugEnterInputStateTest {

    private IyakuhinMasterDTO sampleMaster = new IyakuhinMasterDTO();
    {
        sampleMaster.iyakuhincode = 620000033;
        sampleMaster.name = "カロナール錠３００　３００ｍｇ";
        sampleMaster.yomi = "ｶﾛﾅｰﾙｼﾞｮｳ300";
        sampleMaster.unit = "錠";
        sampleMaster.yakka = 7.90;
        sampleMaster.madoku = 0;
        sampleMaster.kouhatsu = 1;
        sampleMaster.zaikei = 1;
        sampleMaster.validFrom = "2018-04-01";
        sampleMaster.validUpto = "0000-00-00";
    }




}

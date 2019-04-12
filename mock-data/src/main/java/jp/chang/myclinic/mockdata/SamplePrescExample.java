package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.dto.PrescExampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("NonAsciiCharacters")
public class SamplePrescExample {

    public static PrescExampleDTO カロナール内服 = new PrescExampleDTO();
    static {
        PrescExampleDTO example = new PrescExampleDTO();
        example.prescExampleId = 553;
        example.iyakuhincode = 620000033;
        example.amount = "3";
        example.category = 0;
        example.comment = "";
        example.days = 5;
        example.masterValidFrom = "2010-04-01";
        example.usage = "分３　毎食後";
        カロナール内服 = example;
    }

    public static PrescExampleDTO アンブロキソール = new PrescExampleDTO();
    static {
        PrescExampleDTO example = new PrescExampleDTO();
        example.prescExampleId = 846;
        example.iyakuhincode = 620389417;
        example.amount = "3";
        example.category = 0;
        example.comment = "アントブロンと同じ";
        example.days = 5;
        example.masterValidFrom = "2014-04-01";
        example.usage = "分３　毎食後";
        アンブロキソール = example;
    }

    public static PrescExampleDTO デキストロメトルファン = new PrescExampleDTO();

    static {
        PrescExampleDTO example = new PrescExampleDTO();
        example.prescExampleId = 867;
        example.iyakuhincode = 620374101;
        example.amount = "6";
        example.category = 0;
        example.comment = "シーサールと同じ";
        example.days = 5;
        example.masterValidFrom = "2018-04-01";
        example.usage = "分３　毎食後";
        デキストロメトルファン = example;
    }

// template:
//    public static PrescExampleDTO NAME = new PrescExampleDTO();
//    static {
//        PrescExampleDTO example = new PrescExampleDTO();
//        NAME = example;
//    }

}

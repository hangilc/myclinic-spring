package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("NonAsciiCharacters")
public class SampleShinryouMaster {

    public static ShinryouMasterDTO 初診 = new ShinryouMasterDTO();
    static {
        ShinryouMasterDTO master = new ShinryouMasterDTO();
        master.shinryoucode = 111000110;
        master.houkatsukensa = "0";
        master.kensaGroup = "0";
        master.name = "初診料";
        master.oushinkubun = '0';
        master.shuukeisaki = "110";
        master.tensuu = 282;
        master.tensuuShikibetsu = '3';
        master.validFrom = "2018-04-01";
        master.validUpto = "0000-00-00";
        初診 = master;
    }
}

package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShinryouMasterDTO;

import java.util.function.Consumer;

public class ShinryouMasterBuilder {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouMasterBuilder.class);
    private ShinryouMasterDTO result = new ShinryouMasterDTO();

    ShinryouMasterBuilder() {
        result.shinryoucode = G.genid();
        result.validFrom = G.today();
        result.validUpto = G.nullDate();
        result.name = G.gensym();
        result.codeAlpha = 'D';
        result.codeKubun = "007";
        result.codeBu = "03";
        result.codeShou = '2';
        result.roujinTekiyou = '0';
        result.oushinkubun = '0';
        result.houkatsukensa = "00";
        result.kensaGroup = "03";
        result.tensuu = 144;
        result.tensuuShikibetsu = '3';
        result.shuukeisaki = "600";
    }

    public ShinryouMasterDTO build(){
        return result;
    }

    public ShinryouMasterBuilder modify(Consumer<ShinryouMasterDTO> cb){
        cb.accept(result);
        return this;
    }

}

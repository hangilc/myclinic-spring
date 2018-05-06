package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;

import java.util.function.Consumer;

public class IyakuhinMasterBuilder {

    //private static Logger logger = LoggerFactory.getLogger(IyakuhinMasterBuilder.class);
    private IyakuhinMasterDTO result;

    public IyakuhinMasterBuilder() {
        result = new IyakuhinMasterDTO();
        result.iyakuhincode = G.genid();
        result.name = G.gensym();
        result.yomi = G.gensym();
        result.unit = G.gensym();
        result.yakka = 39.1;
        result.madoku = '0';
        result.kouhatsu = 1;
        result.zaikei = 1;
        result.validFrom = G.today();
        result.validUpto = G.nullDate();
    }

    public IyakuhinMasterDTO build(){
        return result;
    }

    public IyakuhinMasterBuilder modify(Consumer<IyakuhinMasterDTO> cb){
        cb.accept(result);
        return this;
    }

}

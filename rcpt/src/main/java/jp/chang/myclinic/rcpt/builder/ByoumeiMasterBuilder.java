package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;

import java.util.function.Consumer;

public class ByoumeiMasterBuilder {

    //private static Logger logger = LoggerFactory.getLogger(ByoumeiMasterBuilder.class);
    private ByoumeiMasterDTO result;

    ByoumeiMasterBuilder() {
        result = new ByoumeiMasterDTO();
        result.shoubyoumeicode = G.genid();
        result.name = G.gensym();
        result.validFrom = G.today();
        result.validUpto = G.nullDate();
    }

    public ByoumeiMasterDTO build(){
        return result;
    }

    public ByoumeiMasterBuilder modify(Consumer<ByoumeiMasterDTO> cb){
        cb.accept(result);
        return this;
    }

}

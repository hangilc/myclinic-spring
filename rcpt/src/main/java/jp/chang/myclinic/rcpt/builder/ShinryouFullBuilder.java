package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShinryouFullDTO;

import java.util.function.Consumer;

public class ShinryouFullBuilder {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouFullBuilder.class);
    private ShinryouFullDTO result;

    public ShinryouFullBuilder() {
        result = new ShinryouFullDTO();
        result.master = new ShinryouMasterBuilder().build();
        result.shinryou = new ShinryouBuilder()
                .modify(s -> s.shinryoucode = result.master.shinryoucode)
                .build();
    }

    public ShinryouFullDTO build(){
        return result;
    }

    public ShinryouFullBuilder modify(Consumer<ShinryouFullDTO> cb){
        cb.accept(result);
        return this;
    }

    public ShinryouFullBuilder setShinryoucode(int shinryoucode){
        result.master.shinryoucode = shinryoucode;
        result.shinryou.shinryoucode = shinryoucode;
        return this;
    }

    public ShinryouFullBuilder setShinryouId(int shinryouId){
        result.shinryou.shinryouId = shinryouId;
        return this;
    }
}

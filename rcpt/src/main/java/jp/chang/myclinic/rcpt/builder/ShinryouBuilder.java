package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShinryouDTO;

import java.util.function.Consumer;

public class ShinryouBuilder {

    private ShinryouDTO result;

    public ShinryouBuilder() {
        result = new ShinryouDTO();
        result.shinryouId = G.genid();
        result.shinryoucode = G.genid();
        result.visitId = G.genid();
    }

    public ShinryouBuilder(Consumer<ShinryouDTO> modifier){
        this();
        modifier.accept(result);
    }

    public ShinryouDTO build(){
        return result;
    }

    public ShinryouBuilder modify(Consumer<ShinryouDTO> cb){
        cb.accept(result);
        return this;
    }

}

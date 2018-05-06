package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DrugFullDTO;

import java.util.function.Consumer;

public class DrugFullBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DrugFullBuilder.class);
    private DrugFullDTO result;

    public DrugFullBuilder() {
        result = new DrugFullDTO();
        result.master = new IyakuhinMasterBuilder().build();
        result.drug =new DrugBuilder().build();
    }

    public DrugFullDTO build(){
        return result;
    }

    public DrugFullBuilder modify(Consumer<DrugFullDTO> cb){
        cb.accept(result);
        return this;
    }

}

package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;

import java.util.function.Consumer;

public class DrugBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DrugBuilder.class);
    private DrugDTO result;

    public DrugBuilder() {
        result = new DrugDTO();
        result.drugId = G.genid();
        result.visitId = G.genid();
        result.iyakuhincode = G.genid();
        result.amount = 3.0;
        result.category = DrugCategory.Naifuku.getCode();
        result.days = 7;
        result.prescribed = 0;
        result.shuukeisaki = 0;
    }

    public DrugDTO build(){
        return result;
    }

    public DrugBuilder modify(Consumer<DrugDTO> cb){
        cb.accept(result);
        return this;
    }

}

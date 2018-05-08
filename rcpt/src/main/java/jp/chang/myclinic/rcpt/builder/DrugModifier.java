package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;

class DrugModifier {

    //private static Logger logger = LoggerFactory.getLogger(DrugModifier.class);
    private DrugDTO drug;

    DrugModifier(DrugDTO drug) {
        this.drug = drug;
    }

    DrugModifier setCategory(DrugCategory category){
        drug.category = category.getCode();
        return this;
    }

}

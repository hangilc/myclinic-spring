package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IntegrationService {

    void broadcastNewText(TextDTO text);
    void setOnNewText(Consumer<TextDTO> handler);
    void broadcastNewDrug(DrugFullDTO drug, DrugAttrDTO attr);
    void setOnNewDrug(BiConsumer<DrugFullDTO, DrugAttrDTO> handler);
    void broadcastNewShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr);
    void setOnNewShinryou(BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> handler);
    void broadcastNewConduct(ConductFullDTO entered);
    void setOnNewConduct(Consumer<ConductFullDTO> handler);

}

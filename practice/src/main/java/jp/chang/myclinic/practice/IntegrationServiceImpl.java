package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IntegrationServiceImpl implements IntegrationService {

    private Consumer<TextDTO> onNewTextHandler = t -> {};

    @Override
    public void broadcastNewText(TextDTO text) {
        onNewTextHandler.accept(text);
    }

    @Override
    public void setOnNewText(Consumer<TextDTO> handler) {
        this.onNewTextHandler = handler;
    }

    private BiConsumer<DrugFullDTO, DrugAttrDTO> onNewDrugHandler = (drug, attr) -> {};

    @Override
    public void broadcastNewDrug(DrugFullDTO drug, DrugAttrDTO attr) {
        onNewDrugHandler.accept(drug, attr);
    }

    @Override
    public void setOnNewDrug(BiConsumer<DrugFullDTO, DrugAttrDTO> handler) {
        this.onNewDrugHandler = handler;
    }

    private BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> onNewShinryouHandler = (s, a) -> {};

    @Override
    public void broadcastNewShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        onNewShinryouHandler.accept(shinryou, attr);
    }

    @Override
    public void setOnNewShinryou(BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> handler) {
        this.onNewShinryouHandler = handler;
    }

    private Consumer<ConductFullDTO> onNewConductHandler = c -> {};

    @Override
    public void broadcastNewConduct(ConductFullDTO entered) {
        onNewConductHandler.accept(entered);
    }

    @Override
    public void setOnNewConduct(Consumer<ConductFullDTO> handler) {
        this.onNewConductHandler = handler;
    }

    private VisitPageHandler visitPageHandler = (p, t, v) -> {};

    @Override
    public void broadcastVisitPage(int page, int totalPages, List<VisitFull2DTO> visits) {
        visitPageHandler.accept(page, totalPages, visits);
    }

    @Override
    public void setVisitPageHandler(VisitPageHandler handler) {
        this.visitPageHandler = handler;
    }
}

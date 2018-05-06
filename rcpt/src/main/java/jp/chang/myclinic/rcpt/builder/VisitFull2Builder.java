package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;

import java.util.ArrayList;
import java.util.function.Consumer;

public class VisitFull2Builder {

    //private static Logger logger = LoggerFactory.getLogger(VisitFull2Builder.class);
    private VisitFull2DTO result;

    public VisitFull2Builder() {
        result = new VisitFull2DTO();
        result.visit = new VisitBuilder().build();
        result.texts = new ArrayList<>();
        result.drugs = new ArrayList<>();
        result.shinryouList = new ArrayList<>();
        result.conducts = new ArrayList<>();
        result.charge = null;
        result.hoken = new HokenBuilder().build();
    }

    public VisitFull2DTO build(){
        return result;
    }

    public VisitFull2Builder modify(Consumer<VisitFull2DTO> cb){
        cb.accept(result);
        return this;
    }

    public VisitFull2Builder addShinryou(ShinryouFullDTO shinryou){
        shinryou.shinryou.visitId = result.visit.visitId;
        result.shinryouList.add(shinryou);
        return this;
    }

    public VisitFull2Builder addDrug(DrugFullDTO drug){
        drug.drug.visitId = result.visit.visitId;
        result.drugs.add(drug);
        return this;
    }

    public VisitFull2Builder setVisitId(int visitId){
        result.visit.visitId = visitId;
        result.texts.forEach(t -> t.visitId = visitId);
        result.drugs.forEach(d -> d.drug.visitId = visitId);
        result.shinryouList.forEach(s -> s.shinryou.visitId = visitId);
        result.conducts.forEach(c -> c.conduct.visitId = visitId);
        if( result.charge != null ){
            result.charge.visitId = visitId;
        }
        return this;
    }

    public VisitFull2Builder setVisitedAt(String at){
        result.visit.visitedAt = at;
        return this;
    }

    public VisitFull2Builder setPatientId(int patientId){
        result.visit.patientId = patientId;
        result.hoken = new HokenBuilder(result.hoken).setPatientId(patientId).build();
        return this;
    }

}

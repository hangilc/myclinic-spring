package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import jp.chang.myclinic.rcpt.builder.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.Consumer;

class Base {

    private static Logger logger = LoggerFactory.getLogger(Base.class);
    ResolvedShinryouMap shinryouMap;
    Common.MasterMaps masterMaps;
    int nerror;

    Base() {
        shinryouMap = TestListener.shinryouMap;
        masterMaps = TestListener.masterMaps;
    }

    Scope createScope() {
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = new PatientDTO();
        scope.resolvedMasterMap = masterMaps.resolvedMap;
        scope.shinryouByoumeiMap = masterMaps.shinryouByoumeiMap;
        scope.api = Service.api;
        return scope;
    }

    ShinryouFullDTO mkShinryouFull(int shinryoucode, Consumer<ShinryouFullDTO> cb){
        return C.createShinryouFull(shinryouFull -> {
            shinryouFull.shinryou = C.createShinryou(s -> s.shinryoucode = shinryoucode);
            shinryouFull.master = C.createShinryouMaster(m -> m.shinryoucode = shinryoucode);
            if( cb != null ) {
                cb.accept(shinryouFull);
            }
        });
    }

    ShinryouFullDTO mkShinryouFull(int shinryoucode) {
        return mkShinryouFull(shinryoucode, null);
    }

}

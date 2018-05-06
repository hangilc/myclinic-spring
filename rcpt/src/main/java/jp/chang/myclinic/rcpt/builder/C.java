package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class C {

    private static Logger logger = LoggerFactory.getLogger(C.class);

    private C() {
    }

    public static <T> T create(Class<T> cls, Consumer<T> cb) {
        try {
            T v = cls.getDeclaredConstructor().newInstance();
            if( cb != null ) {
                cb.accept(v);
            }
            return v;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PatientDTO createPatient(Consumer<PatientDTO> cb) {
        return create(PatientDTO.class, cb);
    }

    public static VisitDTO createVisit(Consumer<VisitDTO> cb) {
        return create(VisitDTO.class, cb);
    }

    public static VisitFull2DTO createVisitFull(Consumer<VisitFull2DTO> cb) {
        return create(VisitFull2DTO.class, cb);
    }

    public static DrugFullDTO createDrugFull(Consumer<DrugFullDTO> cb){
        return create(DrugFullDTO.class, cb);
    }

    public static ShinryouDTO createShinryou(Consumer<ShinryouDTO> cb){
        return create(ShinryouDTO.class, cb);
    }

    public static ShinryouFullDTO createShinryouFull(Consumer<ShinryouFullDTO> cb){
        return create(ShinryouFullDTO.class, cb);
    }

    public static ShinryouMasterDTO createShinryouMaster(Consumer<ShinryouMasterDTO> cb){
        return create(ShinryouMasterDTO.class, cb);
    }

}

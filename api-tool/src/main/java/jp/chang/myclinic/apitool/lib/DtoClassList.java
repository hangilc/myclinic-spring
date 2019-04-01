package jp.chang.myclinic.apitool.lib;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoClassList {

    private static final List<Class<?>> list = List.of(
            ByoumeiMasterDTO.class,
            ChargeDTO.class,
            ConductDTO.class,
            ConductDrugDTO.class,
            ConductKizaiDTO.class,
            ConductShinryouDTO.class,
            DiseaseDTO.class,
            DiseaseAdjDTO.class,
            DrugDTO.class,
            DrugAttrDTO.class,
            GazouLabelDTO.class,
            HotlineDTO.class,
            IntraclinicCommentDTO.class,
            IntraclinicPostDTO.class,
            IntraclinicTagDTO.class,
            IntraclinicTagPostDTO.class,
            IyakuhinMasterDTO.class,
            KizaiMasterDTO.class,
            KouhiDTO.class,
            KoukikoureiDTO.class,
            PatientDTO.class,
            PaymentDTO.class,
            PharmaDrugDTO.class,
            PharmaQueueDTO.class,
            PracticeLogDTO.class,
            PrescExampleDTO.class,
            RoujinDTO.class,
            ShahokokuhoDTO.class,
            ShinryouDTO.class,
            ShinryouAttrDTO.class,
            ShinryouMasterDTO.class,
            ShoukiDTO.class,
            ShuushokugoMasterDTO.class,
            TextDTO.class,
            VisitDTO.class,
            WqueueDTO.class
    );

    public static List<Class<?>> getList() {
        return list;
    }

    public static Class<?> getDtoClassByName(String name){
        for(Class<?> cls: getList()){
            if( cls.getSimpleName().equals(name) ){
                return cls;
            }
        }
        return null;
    }

    public static Map<String, Class<?>> getNameDtoClassMap(){
        Map<String, Class<?>> map = new HashMap<>();
        for(Class<?> cls: getList()){
            map.put(cls.getSimpleName(), cls);
        }
        return map;
    }
}

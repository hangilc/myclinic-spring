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
            DiseaseNewDTO.class,
            DrugDTO.class,
            DrugAttrDTO.class,
            DrugWithAttrDTO.class,
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
            ShinryouWithAttrDTO.class,
            ShoukiDTO.class,
            ShuushokugoMasterDTO.class,
            TextDTO.class,
            VisitDTO.class,
            WqueueDTO.class
    );

    private static Map<String, Class<?>> classMap = new HashMap<>();

    static {
        for(Class<?> cls: list){
            classMap.put(cls.getSimpleName(), cls);
        }
    }

    public static List<Class<?>> getList() {
        return list;
    }

    public static Class<?> getDtoClassByName(String name){
        return classMap.get(name);
    }

    public static Map<String, Class<?>> getNameDtoClassMap(){
        return classMap;
    }
}

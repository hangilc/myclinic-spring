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

    private static List<Class<?>> allDtoList = List.of(
            BatchEnterByNamesRequestDTO.class,
            BatchEnterRequestDTO.class,
            BatchEnterResultDTO.class,
            ByoumeiMasterDTO.class,
            ChargeDTO.class,
            ChargeOptionalDTO.class,
            ClinicInfoDTO.class,
            ConductDrugDTO.class,
            ConductDrugFullDTO.class,
            ConductDTO.class,
            ConductEnterRequestDTO.class,
            ConductFullDTO.class,
            ConductKizaiDTO.class,
            ConductKizaiFullDTO.class,
            ConductShinryouDTO.class,
            ConductShinryouFullDTO.class,
            DiseaseAdjDTO.class,
            DiseaseAdjFullDTO.class,
            DiseaseDTO.class,
            DiseaseExampleDTO.class,
            DiseaseFullDTO.class,
            DiseaseModifyDTO.class,
            DiseaseModifyEndReasonDTO.class,
            DiseaseNewDTO.class,
            DrugAttrDTO.class,
            DrugDTO.class,
            DrugFullDTO.class,
            DrugFullWithAttrDTO.class,
            DrugWithAttrDTO.class,
            EnterConductByNamesRequestDTO.class,
            EnterConductKizaiByNamesRequestDTO.class,
            GazouLabelDTO.class,
            HokenDTO.class,
            HokenListDTO.class,
            HotlineDTO.class,
            IntraclinicCommentDTO.class,
            IntraclinicPostDTO.class,
            IntraclinicPostFullDTO.class,
            IntraclinicPostFullPageDTO.class,
            IntraclinicPostPageDTO.class,
            IntraclinicTagDTO.class,
            IntraclinicTagPostDTO.class,
            IyakuhincodeNameDTO.class,
            IyakuhinMasterDTO.class,
            KizaiMasterDTO.class,
            KouhiDTO.class,
            KoukikoureiDTO.class,
            MeisaiDTO.class,
            MeisaiSectionDTO.class,
            PatientDTO.class,
            PatientHokenDTO.class,
            PatientHokenListDTO.class,
            PatientIdTimeDTO.class,
            PaymentDTO.class,
            PaymentVisitPatientDTO.class,
            PharmaDrugDTO.class,
            PharmaDrugNameDTO.class,
            PharmaQueueDTO.class,
            PharmaQueueFullDTO.class,
            PracticeConfigDTO.class,
            PrescExampleDTO.class,
            PrescExampleFullDTO.class,
            ReferItemDTO.class,
            ResolvedStockDrugDTO.class,
            RoujinDTO.class,
            SectionItemDTO.class,
            ShahokokuhoDTO.class,
            ShinryouAttrDTO.class,
            ShinryouDTO.class,
            ShinryouFullDTO.class,
            ShinryouFullWithAttrDTO.class,
            ShinryouMasterDTO.class,
            ShinryouWithAttrDTO.class,
            ShoukiDTO.class,
            ShuushokugoMasterDTO.class,
            StringResultDTO.class,
            TextDTO.class,
            TextVisitDTO.class,
            TextVisitPageDTO.class,
            TextVisitPatientDTO.class,
            TextVisitPatientPageDTO.class,
            TodaysVisitsWithLogInfoDTO.class,
            UserInfoDTO.class,
            VisitChargePatientDTO.class,
            VisitDrugDTO.class,
            VisitDrugPageDTO.class,
            VisitDTO.class,
            VisitFull2DTO.class,
            VisitFull2PageDTO.class,
            VisitFull2PatientDTO.class,
            VisitFull2PatientPageDTO.class,
            VisitFullDTO.class,
            VisitFullPageDTO.class,
            VisitIdVisitedAtDTO.class,
            VisitPatientDTO.class,
            VisitTextDrugDTO.class,
            VisitTextDrugPageDTO.class,
            WqueueDTO.class,
            WqueueFullDTO.class
    );

    private static Map<String, Class<?>> classMap = new HashMap<>();

    static {
        for (Class<?> cls : list) {
            classMap.put(cls.getSimpleName(), cls);
        }
    }

    public static List<Class<?>> getList() {
        return list;
    }

    public static List<Class<?>> getAllList() {
        return allDtoList;
    }

    public static Class<?> getDtoClassByName(String name) {
        return classMap.get(name);
    }

    public static Map<String, Class<?>> getNameDtoClassMap() {
        return classMap;
    }
}

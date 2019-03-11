package jp.chang.myclinic.apitool.pgsqltables;

import jp.chang.myclinic.backend.dto.IntraclinicTagPostDTO;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.util.HashMap;
import java.util.Map;

class TableToDTOMap {

    private static Map<String, Class<?>> tableMap = new HashMap<>();

    static {
        tableMap.put("byoumei_master", ByoumeiMasterDTO.class);
        tableMap.put("charge", ChargeDTO.class);
        tableMap.put("conduct", ConductDTO.class);
        tableMap.put("conduct_drug", ConductDrugDTO.class);
        tableMap.put("conduct_kizai", ConductKizaiDTO.class);
        tableMap.put("conduct_shinryou", ConductShinryouDTO.class);
        tableMap.put("disease", DiseaseDTO.class);
        tableMap.put("disease_adj", DiseaseAdjDTO.class);
        tableMap.put("drug", DrugDTO.class);
        tableMap.put("drug_attr", DrugAttrDTO.class);
        tableMap.put("gazou_label", GazouLabelDTO.class);
        tableMap.put("hotline", HotlineDTO.class);
        tableMap.put("intraclinic_comment", IntraclinicCommentDTO.class);
        tableMap.put("intraclinic_post", IntraclinicPostDTO.class);
        tableMap.put("intraclinic_tag", IntraclinicTagDTO.class);
        tableMap.put("intraclinic_tag_post", IntraclinicTagPostDTO.class);
        tableMap.put("iyakuhin_master", IyakuhinMasterDTO.class);
        tableMap.put("kizai_master", KizaiMasterDTO.class);
        tableMap.put("kouhi", KouhiDTO.class);
        tableMap.put("koukikourei", KoukikoureiDTO.class);
        tableMap.put("patient", PatientDTO.class);
        tableMap.put("payment", PaymentDTO.class);
        tableMap.put("pharma_drug", PharmaDrugDTO.class);
        tableMap.put("pharma_queue", PharmaQueueDTO.class);
        tableMap.put("practice_log", PracticeLogDTO.class);
        tableMap.put("presc_example", PrescExampleDTO.class);
        tableMap.put("roujin", RoujinDTO.class);
        tableMap.put("shahokokuho", ShahokokuhoDTO.class);
        tableMap.put("shinryou", ShinryouDTO.class);
        tableMap.put("shinryou_attr", ShinryouAttrDTO.class);
        tableMap.put("shinryou_master", ShinryouMasterDTO.class);
        tableMap.put("shouki", ShoukiDTO.class);
        tableMap.put("shuushokugo_master", ShuushokugoMasterDTO.class);
        tableMap.put("text", TextDTO.class);
        tableMap.put("visit", VisitDTO.class);
        tableMap.put("wqueue", WqueueDTO.class);
    }

    public static Class<?> mapToDTO(String table) {
        return tableMap.get(table);
    }
}

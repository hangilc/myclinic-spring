package jp.chang.myclinic.apitool.databasespecifics;

import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PgsqlSpecifics implements DatabaseSpecifics {

    private Helper helper = Helper.getInstance();

    private static Map<Integer, Class<?>> sqlTypeMap = new HashMap<>();

    static {
        sqlTypeMap.put(Types.CHAR, String.class);
        sqlTypeMap.put(Types.NUMERIC, BigDecimal.class);
        sqlTypeMap.put(Types.INTEGER, Integer.class);
        sqlTypeMap.put(Types.SMALLINT, Integer.class);
        sqlTypeMap.put(Types.DATE, LocalDate.class);
        sqlTypeMap.put(Types.VARCHAR, String.class);
        sqlTypeMap.put(Types.TIMESTAMP, LocalDateTime.class);
    }

    @Override
    public Class<?> mapDatabaseClass(int sqlType, String dbTypeName) {
        if( sqlType == Types.OTHER ){
            if( Objects.equals(dbTypeName, "jsonb") ){
                return String.class;
            } else {
                return null;
            }
        } else {
            return sqlTypeMap.get(sqlType);
        }
    }

    private static Map<String, String> dtoFieldMap = new HashMap<>();

    static {
        dtoFieldMap.put("pharma_drug:side_effect", "sideeffect");
        dtoFieldMap.put("intraclinic_comment:comment_id", "id");
        dtoFieldMap.put("intraclinic_post:post_id", "id");
        dtoFieldMap.put("practice_log:practice_log_id", "serialId");
        dtoFieldMap.put("shinryou_master:kensagroup", "kensaGroup");
    }

    @Override
    public String resolveDtoFieldName(String table, String dbColumnName) {
        return dtoFieldMap.getOrDefault(table + ":" + dbColumnName, helper.snakeToCamel(dbColumnName));
    }

    private static Map<String, Class<?>> tableNameToDtoClassMap = new HashMap<>();

    static {
        tableNameToDtoClassMap.put("byoumei_master", ByoumeiMasterDTO.class);
        tableNameToDtoClassMap.put("charge", ChargeDTO.class);
        tableNameToDtoClassMap.put("conduct", ConductDTO.class);
        tableNameToDtoClassMap.put("conduct_drug", ConductDrugDTO.class);
        tableNameToDtoClassMap.put("conduct_kizai", ConductKizaiDTO.class);
        tableNameToDtoClassMap.put("conduct_shinryou", ConductShinryouDTO.class);
        tableNameToDtoClassMap.put("disease", DiseaseDTO.class);
        tableNameToDtoClassMap.put("disease_adj", DiseaseAdjDTO.class);
        tableNameToDtoClassMap.put("drug", DrugDTO.class);
        tableNameToDtoClassMap.put("drug_attr", DrugAttrDTO.class);
        tableNameToDtoClassMap.put("gazou_label", GazouLabelDTO.class);
        tableNameToDtoClassMap.put("hotline", HotlineDTO.class);
        tableNameToDtoClassMap.put("intraclinic_comment", IntraclinicCommentDTO.class);
        tableNameToDtoClassMap.put("intraclinic_post", IntraclinicPostDTO.class);
        tableNameToDtoClassMap.put("intraclinic_tag", IntraclinicTagDTO.class);
        tableNameToDtoClassMap.put("intraclinic_tag_post", IntraclinicTagPostDTO.class);
        tableNameToDtoClassMap.put("iyakuhin_master", IyakuhinMasterDTO.class);
        tableNameToDtoClassMap.put("kizai_master", KizaiMasterDTO.class);
        tableNameToDtoClassMap.put("kouhi", KouhiDTO.class);
        tableNameToDtoClassMap.put("koukikourei", KoukikoureiDTO.class);
        tableNameToDtoClassMap.put("patient", PatientDTO.class);
        tableNameToDtoClassMap.put("payment", PaymentDTO.class);
        tableNameToDtoClassMap.put("pharma_drug", PharmaDrugDTO.class);
        tableNameToDtoClassMap.put("pharma_queue", PharmaQueueDTO.class);
        tableNameToDtoClassMap.put("practice_log", PracticeLogDTO.class);
        tableNameToDtoClassMap.put("presc_example", PrescExampleDTO.class);
        tableNameToDtoClassMap.put("roujin", RoujinDTO.class);
        tableNameToDtoClassMap.put("shahokokuho", ShahokokuhoDTO.class);
        tableNameToDtoClassMap.put("shinryou", ShinryouDTO.class);
        tableNameToDtoClassMap.put("shinryou_attr", ShinryouAttrDTO.class);
        tableNameToDtoClassMap.put("shinryou_master", ShinryouMasterDTO.class);
        tableNameToDtoClassMap.put("shouki", ShoukiDTO.class);
        tableNameToDtoClassMap.put("shuushokugo_master", ShuushokugoMasterDTO.class);
        tableNameToDtoClassMap.put("text", TextDTO.class);
        tableNameToDtoClassMap.put("visit", VisitDTO.class);
        tableNameToDtoClassMap.put("wqueue", WqueueDTO.class);
    }

    @Override
    public Class<?> mapTableNameToDtoClass(String tableName) {
        return tableNameToDtoClassMap.get(tableName);
    }
}

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

    @Override
    public String getCatalog() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getSchema() {
        throw new RuntimeException("not implemented");
    }

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
    public Class<?> getDbColumnClass(String tableName, String columnName, int sqlType, String dbTypeName) {
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
    public String getDtoFieldName(String table, String dbColumnName) {
        return dtoFieldMap.getOrDefault(table + ":" + dbColumnName, helper.snakeToCamel(dbColumnName));
    }

    private static Map<Class<?>, String> dtoClassToTableNameMap = new HashMap<>();

    static {
        dtoClassToTableNameMap.put(ByoumeiMasterDTO.class, "byoumei_master");
        dtoClassToTableNameMap.put(ChargeDTO.class, "charge");
        dtoClassToTableNameMap.put(ConductDTO.class, "conduct");
        dtoClassToTableNameMap.put(ConductDrugDTO.class, "conduct_drug");
        dtoClassToTableNameMap.put(ConductKizaiDTO.class, "conduct_kizai");
        dtoClassToTableNameMap.put(ConductShinryouDTO.class, "conduct_shinryou");
        dtoClassToTableNameMap.put(DiseaseDTO.class, "disease");
        dtoClassToTableNameMap.put(DiseaseAdjDTO.class, "disease_adj");
        dtoClassToTableNameMap.put(DrugDTO.class, "drug");
        dtoClassToTableNameMap.put(DrugAttrDTO.class, "drug_attr");
        dtoClassToTableNameMap.put(GazouLabelDTO.class, "gazou_label");
        dtoClassToTableNameMap.put(HotlineDTO.class, "hotline");
        dtoClassToTableNameMap.put(IntraclinicCommentDTO.class, "intraclinic_comment");
        dtoClassToTableNameMap.put(IntraclinicPostDTO.class, "intraclinic_post");
        dtoClassToTableNameMap.put(IntraclinicTagDTO.class, "intraclinic_tag");
        dtoClassToTableNameMap.put(IntraclinicTagPostDTO.class, "intraclinic_tag_post");
        dtoClassToTableNameMap.put(IyakuhinMasterDTO.class, "iyakuhin_master");
        dtoClassToTableNameMap.put(KizaiMasterDTO.class, "kizai_master");
        dtoClassToTableNameMap.put(KouhiDTO.class, "kouhi");
        dtoClassToTableNameMap.put(KoukikoureiDTO.class, "koukikourei");
        dtoClassToTableNameMap.put(PatientDTO.class, "patient");
        dtoClassToTableNameMap.put(PaymentDTO.class, "payment");
        dtoClassToTableNameMap.put(PharmaDrugDTO.class, "pharma_drug");
        dtoClassToTableNameMap.put(PharmaQueueDTO.class, "pharma_queue");
        dtoClassToTableNameMap.put(PracticeLogDTO.class, "practice_log");
        dtoClassToTableNameMap.put(PrescExampleDTO.class, "presc_example");
        dtoClassToTableNameMap.put(RoujinDTO.class, "roujin");
        dtoClassToTableNameMap.put(ShahokokuhoDTO.class, "shahokokuho");
        dtoClassToTableNameMap.put(ShinryouDTO.class, "shinryou");
        dtoClassToTableNameMap.put(ShinryouAttrDTO.class, "shinryou_attr");
        dtoClassToTableNameMap.put(ShinryouMasterDTO.class, "shinryou_master");
        dtoClassToTableNameMap.put(ShoukiDTO.class, "shouki");
        dtoClassToTableNameMap.put(ShuushokugoMasterDTO.class, "shuushokugo_master");
        dtoClassToTableNameMap.put(TextDTO.class, "text");
        dtoClassToTableNameMap.put(VisitDTO.class, "visit");
        dtoClassToTableNameMap.put(WqueueDTO.class, "wqueue");
    }

    @Override
    public String dtoClassToDbTableName(Class<?> dtoClass) {
        return dtoClassToTableNameMap.computeIfAbsent(dtoClass, key -> {
            String msg = String.format("Cannot convert DTO type (%s) to table name.", dtoClass.getSimpleName());
            throw new RuntimeException(msg);
        });
    }

//    @Override
//    public Class<?> mapTableNameToDtoClass(String tableName) {
//        return tableNameToDtoClassMap.get(tableName);
//    }
}

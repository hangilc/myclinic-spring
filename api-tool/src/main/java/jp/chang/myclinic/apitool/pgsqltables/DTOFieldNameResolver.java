package jp.chang.myclinic.apitool.pgsqltables;

import jp.chang.myclinic.apitool.lib.Helper;

import java.util.HashMap;
import java.util.Map;

class DTOFieldNameResolver {

    private static Map<String, String> fieldMap = new HashMap<>();

    static {
        fieldMap.put("pharma_drug:side_effect", "sideeffect");
        fieldMap.put("intraclinic_comment:comment_id", "id");
        fieldMap.put("intraclinic_post:post_id", "id");
        fieldMap.put("practice_log:practice_log_id", "serialId");
        fieldMap.put("shinryou_master:kensagroup", "kensaGroup");
    }

    private static Helper helper = new Helper();

    static String resolve(String table, String sqlColumnName){
        String ex = fieldMap.getOrDefault(table + ":" + sqlColumnName, null);
        return ex != null ? ex : helper.snakeToCamel(sqlColumnName);
    }

}

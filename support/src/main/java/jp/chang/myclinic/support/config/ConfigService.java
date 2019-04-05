package jp.chang.myclinic.support.config;

import java.util.Map;

public interface ConfigService {
    String getValue(String key);
    void setValue(String key, String value);
    void batchUpdate(Map<String, String> values);
}

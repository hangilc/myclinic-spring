package jp.chang.myclinic.backenddb;

public interface Dialect {
    String isValidAt(String validFrom, String validUpto, String at);
}

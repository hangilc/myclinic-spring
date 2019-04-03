package jp.chang.myclinic.support.kizainames;

import java.util.List;

public interface KizaiNamesService {
    List<String> getCandidateNames(String key);
}

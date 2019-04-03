package jp.chang.myclinic.support.shinryounames;

import java.util.List;

public interface ShinryouNamesService {
    List<String> getCandidateNames(String key);
}

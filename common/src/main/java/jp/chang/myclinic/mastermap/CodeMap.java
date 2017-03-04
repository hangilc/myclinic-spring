package jp.chang.myclinic.mastermap;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by hangil on 2017/03/04.
 */
class CodeMap {
    private ArrayList<CodeMapEntry> entries;

    CodeMap(){
        entries = new ArrayList<>();
    }

    void addEntry(CodeMapEntry entry){
        entries.add(entry);
    }

    void sortByDate(){
        entries.sort(CodeMapEntry::compareTo);
    }

    int resolve(int code, LocalDate at){
        int n = entries.size();
        for(int i=0;i<n;i++){
            CodeMapEntry entry = entries.get(i);
            code = entry.apply(code, at);
        }
        return code;
    }
}

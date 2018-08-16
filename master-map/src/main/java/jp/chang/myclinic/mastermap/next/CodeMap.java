package jp.chang.myclinic.mastermap.next;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeMap {
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

    public int resolve(int code, LocalDate at){
        int n = entries.size();
        for(int i=0;i<n;i++){
            CodeMapEntry entry = entries.get(i);
            code = entry.apply(code, at);
        }
        return code;
    }

    public List<CodeMapEntry> getEntries(){
        return entries.stream().map(CodeMapEntry::new).collect(Collectors.toList());
    }

}

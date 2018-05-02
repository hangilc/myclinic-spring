package jp.chang.myclinic.rcpt.unit;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class HoukatsuKensaItem implements Countable, Extendable<HoukatsuKensaItem>, Mergeable<HoukatsuKensaItem> {

    private static Logger logger = LoggerFactory.getLogger(HoukatsuKensaItem.class);
    private HoukatsuKensaKind kind;
    private Set<Integer> shinryoucodes = new HashSet<>();
    private Map<Integer, ShinryouMasterDTO> masterMap = new HashMap<>();
    private int count;

    HoukatsuKensaItem(ShinryouMasterDTO master) {
        assert HoukatsuKensaKind.fromCode(master.houkatsukensa) != HoukatsuKensaKind.NONE;
        this.kind = HoukatsuKensaKind.fromCode(master.houkatsukensa);
        addMaster(master);
        this.count = 1;
    }

    HoukatsuKensaKind getKind(){
        return kind;
    }

    private void addMaster(ShinryouMasterDTO master){
        assert HoukatsuKensaKind.fromCode(master.houkatsukensa) == kind;
        shinryoucodes.add(master.shinryoucode);
        masterMap.put(master.shinryoucode, master);
    }

    @Override
    public boolean isExtendableWith(HoukatsuKensaItem a) {
        return kind == a.kind;
    }

    @Override
    public void extendWith(HoukatsuKensaItem master){
        master.masterMap.values().forEach(this::addMaster);
    }

    @Override
    public boolean isMergeableWith(HoukatsuKensaItem arg){
        return kind == arg.kind && shinryoucodes.equals(arg.shinryoucodes);
    }

    @Override
    public void incCount(int n){
        count += n;
    }

    @Override
    public int getCount(){
        return count;
    }

    @Override
    public String toString() {
        return "HoukatsuKensaItem{" +
                "kind=" + kind +
                ", shinryoucodes=" + shinryoucodes +
                ", masterMap=" + masterMap +
                ", count=" + count +
                '}';
    }
}

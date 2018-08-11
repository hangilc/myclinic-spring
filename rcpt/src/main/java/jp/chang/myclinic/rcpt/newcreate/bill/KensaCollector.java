package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Globals;
import jp.chang.myclinic.rcpt.newcreate.input.Shinryou;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class KensaCollector {

    private static Logger logger = LoggerFactory.getLogger(KensaCollector.class);
    private ResolvedShinryouMap resolvedShinryouMap;
    private Map<HoukatsuKensaKind, List<Shinryou>> houkatsuMap = new LinkedHashMap<>();
    private List<Shinryou> handanryouList = new ArrayList<>();
    private List<Shinryou> shinryouList = new ArrayList<>();

    KensaCollector(ResolvedShinryouMap resolvedShinryouMap) {
        this.resolvedShinryouMap = resolvedShinryouMap;
    }

    void add(Shinryou shinryou){
        HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(shinryou.houkatsuKensa);
        if( kind != null && kind != HoukatsuKensaKind.NONE ){
            addHoukatsuKensa(kind, shinryou);
        } else if( isHandanryou(shinryou.getShinryoucode()) ) {
            handanryouList.add(shinryou);
        } else {
            shinryouList.add(shinryou);
        }
    }

    public Map<HoukatsuKensaKind, List<Shinryou>> getHoukatsuMap() {
        return houkatsuMap;
    }

    public List<Shinryou> getHandanryouList() {
        return handanryouList;
    }

    public List<Shinryou> getShinryouList() {
        return shinryouList;
    }

    private void addHoukatsuKensa(HoukatsuKensaKind kind, Shinryou shinryou){
        if( houkatsuMap.containsKey(kind) ){
            houkatsuMap.get(kind).add(shinryou);
        } else {
            List<Shinryou> list = new ArrayList<>();
            list.add(shinryou);
            houkatsuMap.put(kind, list);
        }
    }

    private boolean isHandanryou(int shinryoucode){
        ResolvedShinryouMap map = resolvedShinryouMap;
        return shinryoucode == map.尿便検査判断料 ||
                shinryoucode == map.血液検査判断料 ||
                shinryoucode == map.生化Ⅰ判断料 ||
                shinryoucode == map.生化Ⅱ判断料 ||
                shinryoucode == map.免疫検査判断料 ||
                shinryoucode == map.微生物検査判断料 ||
                shinryoucode == map.病理判断料;
    }

    private String rewriteHandanryouName(Shinryou shinryou){
        int shinryoucode = shinryou.shinryoucode;
        ResolvedShinryouMap map = Globals.shinryouMasterMap;
        if( shinryoucode == map.尿便検査判断料 ){
            return "尿";
        } else if( shinryoucode == map.血液検査判断料 ){
            return "血";
        } else if( shinryoucode == map.生化Ⅰ判断料 ){
            return "生Ⅰ";
        } else if( shinryoucode == map.生化Ⅱ判断料 ){
            return "生Ⅱ";
        } else if( shinryoucode == map.免疫検査判断料 ){
            return "免";
        } else if( shinryoucode == map.微生物検査判断料 ){
            return "微";
        } else if( shinryoucode == map.病理判断料 ){
            return "病学";
        } else {
            logger.warn("Unknown handanryou: " + shinryou.name);
            return shinryou.name;
        }
    }


}
